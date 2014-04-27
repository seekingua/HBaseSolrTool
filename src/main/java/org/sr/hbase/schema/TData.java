package org.sr.hbase.schema;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.client.Put;
import org.sr.hbase.schema.value.NullValue;
import org.sr.hbase.schema.value.Value;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhouzhd {2014-04-08 3:07:04}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: TData
 * @Description: bean field value associated with hbase meta data
 * 
 * 
 */
public class TData {

	private byte[] family;
	private Map<byte[], Value> qualValMap = new HashMap<byte[], Value>();

	public TData(byte[] family, Map<byte[], Value> qualValMap) {

		this.family = family;
		this.qualValMap = qualValMap;
	}

	public TData(byte[] family) {

		this.family = family;
	}

	public TData() {
	}

	public byte[] getFamily() {
		return family;
	}

	public void setFamily(byte[] family) {
		this.family = family;
	}

	public Map<byte[], Value> getQualValMap() {
		return qualValMap;
	}

	public void setQualValMap(Map<byte[], Value> qualValMap) {
		this.qualValMap = qualValMap;
	}

	/**
	 * @Author: zhouzhd {2014-04-08 3:07:27}
	 * @Version：
	 * @Title: add
	 * @Description:
	 * 
	 * @param qualifier
	 * @param value
	 */
	public void add(byte[] qualifier, Value value) {

		qualValMap.put(qualifier, value);
	}

	/**
	 * @Author: zhouzhd {2014-04-08 3:07:49}
	 * @Version：
	 * @Title: setPutValue
	 * @Description:
	 * 
	 * @param put
	 * @return
	 */
	public Put setPutValue(Put put) {

		try {
			if (put == null) {
				return null;
			}
			for (byte[] qualifier : qualValMap.keySet()) {
				if (!qualValMap.get(qualifier).getType().equals(NullValue.class)) {
					put.add(family, qualifier, qualValMap.get(qualifier).toBytes());
				}
			}
			return put;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
