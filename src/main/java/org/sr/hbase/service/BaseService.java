package org.sr.hbase.service;

import java.util.List;

import org.apache.hadoop.hbase.filter.Filter;
import org.apache.solr.client.solrj.SolrQuery;
import org.sr.hbase.exception.HBaseException;
import org.sr.hbase.exception.SolrException;
import org.sr.hbase.schema.Page;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhouzhd {2014-03-26 4:10:20}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: BaseService
 * @Description: service for handling hbase or solr data
 * 
 * 
 * @param <T>
 */
public interface BaseService<T> {
	/**
	 * @Author: zhouzhd {2014-03-28 9:53:41}
	 * @Version：
	 * @Title: createTable
	 * @Description:
	 * 
	 */
	public void createTable();

	/**
	 * @Author: zhouzhd {2014-03-28 9:53:44}
	 * @Version：
	 * @Title: createTableIFNotExists
	 * @Description:
	 * 
	 */
	public void createTableIFNotExists();

	/**
	 * @Author: zhouzhd {2014-03-28 9:53:58}
	 * @Version：
	 * @Title: deleteTable
	 * @Description:
	 * 
	 */
	public void deleteTable();

	/**
	 * @Author: zhouzhd {2014-03-28 9:53:49}
	 * @Version：
	 * @Title: deleteByIdHBase
	 * @Description:
	 * 
	 * @param rowkey
	 */
	public void deleteByIdHBase(String rowkey);

	/**
	 * @Author: zhouzhd {2014-03-28 2:08:37}
	 * @Version：
	 * @Title: deleteByIdSolr
	 * @Description:
	 * 
	 * @param rowkey
	 */
	public void deleteByIdSolr(String rowkey);

	/**
	 * @Author: zhouzhd {2014-03-28 2:08:34}
	 * @Version：
	 * @Title: deleteByIdBoth
	 * @Description:
	 * 
	 * @param rowkey
	 */
	public void deleteByIdBoth(String rowkey);

	/**
	 * @Author: zhouzhd {2014-03-27 12:52:40}
	 * @Version：
	 * @Title: saveHBase
	 * @Description:
	 * 
	 * @param data
	 * @throws HBaseException
	 */
	public void saveHBase(T data) throws HBaseException;

	/**
	 * @Author: zhouzhd {2014-03-28 2:56:11}
	 * @Version：
	 * @Title: saveSolr
	 * @Description:
	 * 
	 * @param data
	 * @throws SolrException
	 */
	public void saveSolr(T data) throws SolrException;

	/**
	 * @Author: zhouzhd {2014-03-28 2:56:14}
	 * @Version：
	 * @Title: saveBoth
	 * @Description:
	 * 
	 * @param data
	 */
	public void saveBoth(T data);

	/**
	 * @Author: zhouzhd {2014-03-28 9:53:55}
	 * @Version：
	 * @Title: updateHBase
	 * @Description:
	 * 
	 * @param data
	 */
	public void updateHBase(T data);

	/**
	 * @Author: zhouzhd {2014-03-28 2:09:30}
	 * @Version：
	 * @Title: updateSolr
	 * @Description:
	 * 
	 * @param data
	 */
	public void updateSolr(T data);

	/**
	 * @Author: zhouzhd {2014-03-28 2:52:47}
	 * @Version：
	 * @Title: updateBoth
	 * @Description:
	 * 
	 * @param data
	 */
	public void updateBoth(T data);

	/**
	 * @Author: zhouzhd {2014-04-08 9:55:47}
	 * @Version：
	 * @Title: findAll
	 * @Description:
	 * 
	 * @param startRow
	 * @param stopRow
	 * @return
	 */
	public List<T> findAll(byte[] startRow, byte[] stopRow);

	/**
	 * @Author: zhouzhd {2014-04-08 9:55:38}
	 * @Version：
	 * @Title: findByFilter
	 * @Description:
	 * 
	 * @param filter
	 * @param startRow
	 * @param stopRow
	 * @return
	 */
	public List<T> findByFilter(Filter filter, byte[] startRow, byte[] stopRow);

	/**
	 * @Author: zhouzhd {2014-03-27 12:50:26}
	 * @Version：
	 * @Title: findById
	 * @Description:
	 * 
	 * @param id
	 * @return
	 */
	public T findById(String id);

	/**
	 * @Author: zhouzhd {2014-04-08 2:24:01}
	 * @Version：
	 * @Title: findBySolr
	 * @Description:
	 * 
	 * @param fieldArray, field array list with no id
	 * @param filterArray
	 * @param orderField
	 * @param order
	 * @return
	 */
	public List<T> findBySolr(String[] fieldArray, String[] filterArray, String orderField, SolrQuery.ORDER order);

	/**
	 * @Author: zhouzhd {2014-04-08 10:52:25}
	 * @Version：
	 * @Title: pageBySolr
	 * @Description:
	 * 
	 * @param fieldArray
	 * @param filterArray
	 * @param orderField
	 * @param order
	 * @param page
	 * @return
	 */
	public Page<T> pageBySolr(String[] fieldArray, String[] filterArray, String orderField, SolrQuery.ORDER order, Page<T> page);

}
