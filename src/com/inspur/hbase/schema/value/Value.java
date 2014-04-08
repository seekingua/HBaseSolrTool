package com.inspur.hbase.schema.value;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhengde zhou {2014-03-27 11:38:53}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: Value
 * @Description:
 * 
 * 
 */
public interface Value {

	public byte[] toBytes();

	public String getType();

}
