package com.inspur.hbase.schema.value;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhengde zhou {2014-03-27 11:38:26}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: IntValue
 * @Description:
 * 
 * 
 */
public class IntValue implements Value {

	private int intValue;

	public IntValue(int intValue) {
		super();
		this.intValue = intValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	@Override
	public byte[] toBytes() {
		return Bytes.toBytes(intValue);
	}

	@Override
	public String getType() {
		return "intValue";
	}

}
