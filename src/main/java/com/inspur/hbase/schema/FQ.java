package com.inspur.hbase.schema;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhouzhd {2014-04-08 3:06:42}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: FQ
 * @Description: identifies the family and qulifier
 * 
 * 
 */
public class FQ {

	private byte[] family = null;
	private byte[] qualifier = null;

	public FQ() {
	}

	public FQ(byte[] family, byte[] qualifier) {

		this.family = family;
		this.qualifier = qualifier;
	}

	public byte[] getFamily() {

		return family;
	}

	public void setFamily(byte[] family) {

		this.family = family;
	}

	public byte[] getQualifier() {

		return qualifier;
	}

	public void setQualifier(byte[] qualifier) {

		this.qualifier = qualifier;
	}
}
