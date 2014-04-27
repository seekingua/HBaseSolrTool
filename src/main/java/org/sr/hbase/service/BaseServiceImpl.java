package org.sr.hbase.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.sr.hbase.exception.HBaseException;
import org.sr.hbase.respository.HBaseSolrRespository;
import org.sr.hbase.schema.HBaseWrap;
import org.sr.hbase.schema.Page;
import org.sr.hbase.schema.value.StrValue;
import org.sr.hbase.util.Util;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhouzhd {2014-04-08 1:47:42}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: BaseServiceImpl
 * @Description: service implements
 * 
 * 
 * @param <T>
 */
public class BaseServiceImpl<T> implements BaseService<T> {

	Log LOG = LogFactory.getLog(BaseServiceImpl.class);
	Class<T> tClass;
	private HBaseSolrRespository hsResp;
	private HBaseWrap<T> hbaseWrap = null;

	public BaseServiceImpl(Class<T> tClass, HBaseSolrRespository hbaseRes) throws HBaseException {

		this.tClass = tClass;
		this.hsResp = hbaseRes;
		hbaseWrap = new HBaseWrap<T>(tClass);
	}

	@Override
	public void createTable() {

		if (hsResp.isTableExists(hbaseWrap.getTableName())) {
			hsResp.deleteTable(hbaseWrap.getTableName());
		}
		hsResp.createTable(hbaseWrap.getTableDescriptor());
	}

	@Override
	public void createTableIFNotExists() {

		if (!hsResp.isTableExists(hbaseWrap.getTableName())) {
			hsResp.createTable(hbaseWrap.getTableDescriptor());
		}
	}

	@Override
	public void deleteTable() {

		hsResp.deleteTable(hbaseWrap.getTableName());
	}

	@Override
	public void deleteByIdHBase(String rowkey) {

		Delete delete = new Delete(Bytes.toBytes(rowkey));
		try {
			hsResp.deleteHBase(Bytes.toBytes(hbaseWrap.getTableName()), delete);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteByIdSolr(String rowkey) {

		try {
			hsResp.deleteSolr(rowkey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteByIdBoth(String rowkey) {

		this.deleteByIdHBase(rowkey);
		this.deleteByIdSolr(rowkey);
	}

	@Override
	public void saveHBase(T data) {

		try {
			if (data.getClass().equals(tClass)) {
				hbaseWrap.setQualifierData(data);
				hbaseWrap.saveHBase(hsResp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveSolr(T data) {

		if (data.getClass().equals(tClass)) {
			hbaseWrap.saveSolr(hsResp, data);
		}
	}

	@Override
	public void saveBoth(T data) {

		this.saveHBase(data);
		this.saveSolr(data);
	}

	@Override
	public void updateHBase(T data) {

		saveHBase(data);
	}

	@Override
	public void updateSolr(T data) {

		try {
			if (data.getClass().equals(tClass)) {
				hbaseWrap.updateSolr(hsResp, data, this.findById(String.valueOf(Util.getFromField(data, hbaseWrap.getRowkeyField()).toString())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateBoth(T data) {

		this.updateHBase(data);
		this.updateSolr(data);
	}

	@Override
	public List<T> findAll(byte[] startRow, byte[] stopRow) {

		return hbaseWrap.findAll(hsResp, startRow, stopRow);
	}

	@Override
	public List<T> findByFilter(Filter filter, byte[] startRow, byte[] stopRow) {

		return hbaseWrap.findByFilter(hsResp, filter, startRow, stopRow);
	}

	@Override
	public T findById(String id) {

		return hbaseWrap.findById(new StrValue(id), hsResp);
	}

	@Override
	public List<T> findBySolr(String[] fieldArray, String[] filterArray, String orderField, ORDER order) {

		try {
			if (orderField == null) {
				orderField = hbaseWrap.getRowkeyField().getName();
				order = SolrQuery.ORDER.desc;
			}
			SolrDocumentList sdl = hsResp.findBySolr(orderField, filterArray, orderField, order);
			List<Get> getList = new ArrayList<Get>();
			Get get = null;
			for (SolrDocument solrDocument : sdl) {
				get = new Get(Bytes.toBytes(solrDocument.getFieldValue(orderField).toString()));
				for (String field : fieldArray) {
					get.addColumn(hbaseWrap.getFfqMap().get(tClass.getDeclaredField(field)).getFamily(), hbaseWrap.getFfqMap().get(tClass.getDeclaredField(field)).getQualifier());
				}
				getList.add(get);
			}
			return hbaseWrap.find(hsResp, getList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<T> pageBySolr(String[] fieldArray, String[] filterArray, String orderField, ORDER order, Page<T> page) {

		try {
			if (orderField == null) {
				orderField = hbaseWrap.getRowkeyField().getName();
				order = SolrQuery.ORDER.desc;
			}
			SolrDocumentList sdl = hsResp.pageBySolr(hbaseWrap.getRowkeyField().getName(), filterArray, orderField, order, page);
			List<Get> getList = new ArrayList<Get>();
			Get get = null;
			for (SolrDocument solrDocument : sdl) {
				get = new Get(Bytes.toBytes(solrDocument.getFieldValue(orderField).toString()));
				for (String field : fieldArray) {
					get.addColumn(hbaseWrap.getFfqMap().get(tClass.getDeclaredField(field)).getFamily(), hbaseWrap.getFfqMap().get(tClass.getDeclaredField(field)).getQualifier());
				}
				getList.add(get);
			}
			page.setDataList(hbaseWrap.find(hsResp, getList));
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
