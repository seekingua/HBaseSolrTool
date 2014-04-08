package com.inspur.hbase.schema;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: seeker {2014-04-08 3:06:11}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: DataType
 * @Description: identifies the field type
 * 
 * 
 */
public class DataType {

	public static final int SKIP = -1;
	public static final int PRIMITIVE = 0;
	public int datatype;
	public Class<?> fieldClass;

	public DataType(int datatype, Class<?> fieldClass) {

		this.datatype = datatype;
		this.fieldClass = fieldClass;
	}

	public boolean isSkip() {

		return datatype == SKIP;
	}

	public boolean isPrimitive() {

		return datatype == PRIMITIVE;
	}
}
