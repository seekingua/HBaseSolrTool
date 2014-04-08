package com.inspur.hbase.schema;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.common.SolrInputDocument;

import com.inspur.hbase.annotation.HColumn;
import com.inspur.hbase.annotation.HTable;
import com.inspur.hbase.exception.HBaseException;
import com.inspur.hbase.respository.HBaseSolrRespository;
import com.inspur.hbase.schema.value.Value;
import com.inspur.hbase.schema.value.ValueFactory;
import com.inspur.hbase.util.Util;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhengde zhou {2014-04-08 11:31:02}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: HBaseWrap
 * @Description: handle the bean class for hbase mapping
 * 
 * 
 * @param <T>
 */
public class HBaseWrap<T> {

	Log LOG = LogFactory.getLog(HBaseWrap.class);

	private String tableName;

	private Map<Field, FQ> ffqMap;
	private Map<Field, DataType> fdMap;
	private Set<Field> indexSet;
	private Set<byte[]> familySet;
	private Map<Field, TData> ftdataMap;

	private Field rowkeyField;
	private Class<?> tClass;

	private Value rowkey;

	/**
	 * @Author: zhengde zhou {2014-04-08 12:56:02}
	 * @Version：
	 * @Description:
	 * 
	 * @param tClass
	 * @throws HBaseException
	 */
	public HBaseWrap(Class<T> tClass) throws HBaseException {

		this.tClass = tClass;
		ffqMap = new HashMap<Field, FQ>();
		fdMap = new HashMap<Field, DataType>();
		indexSet = new HashSet<Field>();
		familySet = new HashSet<byte[]>();
		ftdataMap = new HashMap<Field, TData>();
		this.init();
	}

	/**
	 * @Author: zhengde zhou {2014-04-08 12:53:01}
	 * @Version：
	 * @Title: init
	 * @Description:
	 * 
	 * @throws HBaseException
	 */
	private void init() throws HBaseException {

		HTable htable = (HTable) tClass.getAnnotation(HTable.class);

		// init tableName from annotation, class name if table annotation is default value
		if (htable == null || htable.table().length() == 0) {
			tableName = tClass.getSimpleName();
		} else {
			tableName = htable.table();
		}

		// init fdMap,indexSet,rowKeyField,ffqMap
		HColumn hbaseColumn = null;
		FQ fqTemp = null;
		for (Field field : tClass.getDeclaredFields()) {
			hbaseColumn = field.getAnnotation(HColumn.class);
			if (hbaseColumn == null) { //
				fdMap.put(field, new DataType(DataType.SKIP, null));
				continue;
			}
			fdMap.put(field, new DataType(DataType.PRIMITIVE, field.getType()));
			if (hbaseColumn.index()) {
				indexSet.add(field);
			}
			if (hbaseColumn.id()) {
				rowkeyField = field;
				continue;
			}
			fqTemp = getFamilyQualifier(hbaseColumn, field);
			familySet.add(fqTemp.getFamily());
			ftdataMap.put(field, new TData(fqTemp.getFamily()));
			ffqMap.put(field, fqTemp);
		}
	}

	/**
	 * @Author: zhengde zhou {2014-04-08 12:52:26}
	 * @Version：
	 * @Title: getTableDescriptor
	 * @Description:
	 * 
	 * @return
	 */
	public HTableDescriptor getTableDescriptor() {

		HTableDescriptor td = new HTableDescriptor(Bytes.toBytes(tableName));
		for (byte[] familyTemp : familySet) {
			td.addFamily(new HColumnDescriptor(familyTemp));
		}
		return td;
	}

	/**
	 * @Author: zhengde zhou {2014-04-08 12:52:44}
	 * @Version：
	 * @Title: getFamilyQualifier
	 * @Description:
	 * 
	 * @param hcolumn
	 * @param field
	 * @return
	 * @throws HBaseException
	 */
	private FQ getFamilyQualifier(HColumn hcolumn, Field field) throws HBaseException {

		String family = hcolumn.family().length() == 0 ? field.getName() : hcolumn.family();
		String qualifier = hcolumn.qualifier().length() == 0 ? field.getName() : hcolumn.qualifier();
		return new FQ(Bytes.toBytes(family), Bytes.toBytes(qualifier));
	}

	/**
	 * @Author: zhengde zhou {2014-04-08 1:23:00}
	 * @Version：
	 * @Title: getObject
	 * @Description:
	 * 
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public T getObject(Result result) throws Exception {

		Object instance = tClass.newInstance();
		for (Field field : tClass.getDeclaredFields()) {
			if (field.equals(rowkeyField)) {
				Util.setToField(instance, field, ValueFactory.CreateObject(field.getType(), result.getRow()));
				continue;
			}
			if (fdMap.get(field).isSkip()) {
				continue;
			} else {
				byte[] value = result.getValue(ffqMap.get(field).getFamily(), ffqMap.get(field).getQualifier());
				if (value != null) {
					Util.setToField(instance, field, ValueFactory.CreateObject(fdMap.get(field).fieldClass, value));
				}
			}
		}
		return (T) instance;
	}

	/**
	 * @Author: zhengde zhou {2014-04-08 1:43:58}
	 * @Version：
	 * @Title: setQualifierData
	 * @Description:
	 * 
	 * @param instance
	 * @throws Exception
	 */
	public void setQualifierData(T instance) throws Exception {

		FQ fqTemp = null;
		for (Field field : instance.getClass().getDeclaredFields()) {
			if (rowkeyField.equals(field)) {
				rowkey = ValueFactory.Create(Util.getFromField(instance, field));
				continue;
			}
			fqTemp = ffqMap.get(field);
			Value value = ValueFactory.Create(Util.getFromField(instance, field));
			ftdataMap.get(field).add(fqTemp.getQualifier(), value);
		}
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Field getRowkeyField() {
		return rowkeyField;
	}

	public void setRowkeyField(Field rowkeyField) {
		this.rowkeyField = rowkeyField;
	}

	public Map<Field, FQ> getFfqMap() {
		return ffqMap;
	}

	public void setFfqMap(Map<Field, FQ> ffqMap) {
		this.ffqMap = ffqMap;
	}

	/**
	 * @Author: zhengde zhou {2014-03-27 12:35:33}
	 * @Version：
	 * @Title: saveHBase
	 * @Description:
	 * 
	 * @param hbaseResp
	 */
	public void saveHBase(HBaseSolrRespository hbaseResp) {

		try {
			Put put = new Put(rowkey.toBytes());
			for (Field field : ftdataMap.keySet()) {
				ftdataMap.get(field).setPutValue(put);
			}
			hbaseResp.saveHBase(Bytes.toBytes(tableName), put);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Author: zhengde zhou {2014-03-28 4:01:48}
	 * @Version：
	 * @Title: saveSolr
	 * @Description:
	 * 
	 * @param hbaseResp
	 * @param data
	 */
	public void saveSolr(HBaseSolrRespository hbaseResp, T data) {

		try {
			SolrInputDocument sid = new SolrInputDocument();
			for (Field field : indexSet) {
				sid.addField(field.getName(), Util.getFromField(data, field));
			}
			hbaseResp.saveSolr(sid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Author: zhengde zhou {2014-03-28 4:01:32}
	 * @Version：
	 * @Title: updateSolr
	 * @Description:
	 * 
	 * @param hbaseResp
	 * @param data
	 */
	public void updateSolr(HBaseSolrRespository hbaseResp, T data, T oldData) {

		try {
			SolrInputDocument sid = new SolrInputDocument();
			Object object = null;
			for (Field field : indexSet) {
				object = Util.getFromField(data, field);
				if (object != null) {
					sid.addField(field.getName(), object);
				} else {
					sid.addField(field.getName(), Util.getFromField(oldData, field));
				}
			}
			hbaseResp.saveSolr(sid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Author: zhengde zhou {2014-04-08 9:58:26}
	 * @Version：
	 * @Title: findAll
	 * @Description:
	 * 
	 * @param hbaseResp
	 * @param startRow
	 * @param stopRow
	 * @return
	 */
	public List<T> findAll(HBaseSolrRespository hbaseResp, byte[] startRow, byte[] stopRow) {

		List<T> tList = new ArrayList<T>();
		try {
			for (Result result : hbaseResp.findAll(tableName, startRow, stopRow)) {
				tList.add(getObject(result));
			}
			return tList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<T> find(HBaseSolrRespository hbaseResp, List<Get> getList) {

		List<T> tList = new ArrayList<T>();
		try {
			for (Result result : hbaseResp.find(tableName, getList)) {
				tList.add(getObject(result));
			}
			return tList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @Author: zhengde zhou {2014-04-08 9:58:38}
	 * @Version：
	 * @Title: findByFilter
	 * @Description:
	 * 
	 * @param hbaseResp
	 * @param filter
	 * @param startRow
	 * @param stopRow
	 * @return
	 */
	public List<T> findByFilter(HBaseSolrRespository hbaseResp, Filter filter, byte[] startRow, byte[] stopRow) {

		List<T> tList = new ArrayList<T>();
		try {
			for (Result result : hbaseResp.findByFilter(tableName, filter, startRow, stopRow)) {
				tList.add(getObject(result));
			}
			return tList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @Author: zhengde zhou {2014-03-27 12:47:37}
	 * @Version：
	 * @Title: findById
	 * @Description:
	 * 
	 * @param id
	 * @param hbaseResp
	 * @return
	 */
	public T findById(Value id, HBaseSolrRespository hbaseResp) {

		Get get = new Get(id.toBytes());
		Result result = null;
		try {
			result = hbaseResp.find(Bytes.toBytes(tableName), get);
			if (result != null) {
				return getObject(result);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
