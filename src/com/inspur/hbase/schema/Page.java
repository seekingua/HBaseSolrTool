package com.inspur.hbase.schema;

import java.util.List;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhengde zhou {2014-04-09 12:13:55}
 * @Mail: zhouzhd@inspur.com
 * 
 * @ClassName: Page
 * @Description: page bean for solr query
 * 
 * 
 * @param <T>
 */
public class Page<T> {

	private List<T> dataList;
	private int pageSize = 20;
	private int current = 0;
	private int total;
	private int recordCount;

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
		this.total = recordCount / this.pageSize + (recordCount % this.pageSize == 0 ? 0 : 1);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getRecordCount() {
		return recordCount;
	}
}
