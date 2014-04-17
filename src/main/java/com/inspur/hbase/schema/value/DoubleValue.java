package com.inspur.hbase.schema.value;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhouzhd {2014-03-27 11:37:40}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: DoubleValue
 * @Description:
 * 
 * 
 */
public class DoubleValue implements Value {

	private double doubleValue;

	public DoubleValue(double doubleValue) {
		super();
		this.doubleValue = doubleValue;
	}

	public double getIntValue() {
		return doubleValue;
	}

	public void setIntValue(double intValue) {
		this.doubleValue = intValue;
	}

	@Override
	public byte[] toBytes() {
		return Bytes.toBytes(doubleValue);
	}

	@Override
	public String getType() {
		return "Double Value";
	}

}
