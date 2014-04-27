package org.sr.hbase.schema.value;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhouzhd {2014-03-27 11:38:45}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: StringValue
 * @Description:
 * 
 * 
 */
public class StrValue implements Value {

	private String stringValue;

	public StrValue(String stringValue) {
		super();
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public byte[] toBytes() {
		return Bytes.toBytes(stringValue);
	}

	@Override
	public String getType() {
		return "String Value";
	}

}
