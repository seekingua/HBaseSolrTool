package com.inspur.hbase.exception;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhouzhd {2014-04-08 11:28:50}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: HBaseException
 * @Description: hbase exception
 * 
 * 
 */
public class SolrException extends Exception {

	private static final long serialVersionUID = 3926933597980222302L;

	public SolrException(String trace) {
		super(trace);
	}
}
