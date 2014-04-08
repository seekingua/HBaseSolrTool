package com.inspur.hbase.schema.value;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhengde zhou {2014-03-27 11:38:14}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: FloatValue
 * @Description:
 * 
 * 
 */
public class FloatValue implements Value {

	private float floatValue;

	public FloatValue(float floatValue) {
		super();
		this.floatValue = floatValue;
	}

	public float getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
	}

	@Override
	public byte[] toBytes() {
		return Bytes.toBytes(floatValue);
	}

	@Override
	public String getType() {
		return "Float Value";
	}

}
