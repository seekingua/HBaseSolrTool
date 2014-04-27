package org.sr.hbase.exception;

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
public class HBaseException extends Exception {

	private static final long serialVersionUID = 5728364902098690764L;

	public HBaseException(String trace) {
		super(trace);
	}
}
