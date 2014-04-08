package com.inspur.hbase.schema.value;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhengde zhou {2014-03-27 11:38:35}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: NullValue
 * @Description:
 * 
 * 
 */
public class NullValue implements Value {

	@Override
	public byte[] toBytes() {
		return null;
	}

	@Override
	public String getType() {
		return "Null Value";
	}

}
