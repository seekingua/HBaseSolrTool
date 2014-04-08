package com.inspur.hbase.respository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.inspur.hbase.schema.Page;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: seeker {2014-04-08  2:21:03}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: HBaseSolrRespository
 * @Description:
 * 
 * 
 */
/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: seeker {2014-04-08 2:22:47}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: HBaseSolrRespository
 * @Description: respository just like spring jpa respository
 * 
 * 
 */
public class HBaseSolrRespository {

	private static HTablePool pool;
	private static HBaseAdmin hbadmin;
	private int batchSize = 15;
	private int cacheSize = 20;
	private int poolSize = 50;
	private int queueSize = 500;
	private int threadCount = 4;
	private HttpSolrServer querySolr;
	private ConcurrentUpdateSolrServer updateSolr;
	private static final Configuration conf = HBaseConfiguration.create();

	/**
	 * @Author: seeker {2014-04-08 2:21:13}
	 * @Version：
	 * @Description:
	 * 
	 * @param poolSize
	 * @param batchSize
	 * @param cacheSize
	 * @param solrUrl
	 * @param queueSize
	 * @param threadCount
	 */
	public HBaseSolrRespository(int poolSize, int batchSize, int cacheSize, String solrUrl, int queueSize, int threadCount) {

		try {
			this.batchSize = batchSize;
			this.cacheSize = cacheSize;
			pool = new HTablePool(conf, poolSize);
			hbadmin = new HBaseAdmin(conf);
			querySolr = new HttpSolrServer(solrUrl);
			updateSolr = new ConcurrentUpdateSolrServer(solrUrl, queueSize, threadCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Author: seeker {2014-04-08 2:21:22}
	 * @Version：
	 * @Description:
	 * 
	 * @param solrUrl
	 */
	public HBaseSolrRespository(String solrUrl) {

		try {
			pool = new HTablePool(conf, poolSize);
			hbadmin = new HBaseAdmin(conf);
			querySolr = new HttpSolrServer(solrUrl);
			updateSolr = new ConcurrentUpdateSolrServer(solrUrl, queueSize, threadCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Author: seeker {2014-04-08 2:21:28}
	 * @Version：
	 * @Title: createTable
	 * @Description:
	 * 
	 * @param td
	 */
	public void createTable(HTableDescriptor td) {

		try {
			hbadmin.createTable(td);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Author: seeker {2014-04-08 2:21:36}
	 * @Version：
	 * @Title: isTableExists
	 * @Description:
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean isTableExists(final String tableName) {

		try {
			return hbadmin.tableExists(tableName);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @Author: seeker {2014-04-08 2:21:41}
	 * @Version：
	 * @Title: deleteTable
	 * @Description:
	 * 
	 * @param tableName
	 */
	public void deleteTable(final String tableName) {

		try {
			hbadmin.disableTable(tableName);
			hbadmin.deleteTable(tableName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Author: seeker {2014-04-08 9:53:26}
	 * @Version：
	 * @Title: findAll
	 * @Description:
	 * 
	 * @param tablename
	 * @param startRow
	 * @param stopRow
	 * @return
	 * @throws Exception
	 */
	public List<Result> findAll(String tablename, byte[] startRow, byte[] stopRow) throws Exception {

		List<Result> resultList = new ArrayList<Result>();
		HTableInterface hti = pool.getTable(tablename);
		ResultScanner resultScanner = null;
		try {
			Scan scan = new Scan();
			scan.setBatch(batchSize);
			scan.setCaching(cacheSize);
			if (startRow != null) {
				scan.setStartRow(startRow);
			}
			if (stopRow != null) {
				scan.setStopRow(stopRow);
			}
			resultScanner = hti.getScanner(scan);
			int i = 0;
			for (Result result : resultScanner) {
				if (i++ > 1000) {
					throw new Exception("too many records,stop query");
				}
				resultList.add(result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			resultScanner.close();
			hti.close();
		}
		return resultList;
	}

	/**
	 * @Author: seeker {2014-04-08 9:54:31}
	 * @Version：
	 * @Title: findByFilter
	 * @Description:
	 * 
	 * @param tablename
	 * @param filter
	 * @param startRow
	 * @param stopRow
	 * @return
	 * @throws Exception
	 */
	public List<Result> findByFilter(String tablename, Filter filter, byte[] startRow, byte[] stopRow) throws Exception {

		List<Result> resultList = new ArrayList<Result>();
		HTableInterface hti = pool.getTable(tablename);
		ResultScanner resultScanner = null;
		try {
			Scan scan = new Scan();
			scan.setBatch(batchSize);
			scan.setCaching(cacheSize);
			scan.setFilter(filter);
			if (startRow != null) {
				scan.setStartRow(startRow);
			}
			if (stopRow != null) {
				scan.setStopRow(stopRow);
			}
			resultScanner = hti.getScanner(scan);
			int i = 0;
			for (Result result : resultScanner) {
				if (i++ > 1000) {
					throw new Exception("too many records,stop query");
				}
				resultList.add(result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			resultScanner.close();
			hti.close();
		}
		return resultList;
	}

	/**
	 * @Author: seeker {2014-04-08 2:22:04}
	 * @Version：
	 * @Title: find
	 * @Description:
	 * 
	 * @param tablename
	 * @param get
	 * @return
	 * @throws IOException
	 */
	public Result find(byte[] tablename, Get get) throws IOException {

		HTableInterface hti = pool.getTable(tablename);
		Result result = null;
		try {
			result = hti.get(get);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			hti.close();
		}
		return result;
	}

	/**
	 * @Author: seeker {2014-04-08 2:22:12}
	 * @Version：
	 * @Title: find
	 * @Description:
	 * 
	 * @param tablename
	 * @param getList
	 * @return
	 * @throws IOException
	 */
	public List<Result> find(String tablename, List<Get> getList) throws IOException {

		HTableInterface hti = pool.getTable(Bytes.toBytes(tablename));
		List<Result> resultList = new ArrayList<Result>();
		try {
			resultList = Arrays.asList(hti.get(getList));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			hti.close();
		}
		return resultList;
	}

	/**
	 * @Author: seeker {2014-04-08 2:22:22}
	 * @Version：
	 * @Title: deleteHBase
	 * @Description:
	 * 
	 * @param tablename
	 * @param delete
	 * @throws IOException
	 */
	public void deleteHBase(byte[] tablename, Delete delete) throws IOException {

		HTableInterface hti = pool.getTable(tablename);
		try {
			hti.delete(delete);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			hti.close();
		}
	}

	/**
	 * @Author: seeker {2014-04-08 2:22:30}
	 * @Version：
	 * @Title: deleteSolr
	 * @Description:
	 * 
	 * @param rowkey
	 */
	public void deleteSolr(String rowkey) {

		try {
			updateSolr.deleteById(rowkey);
			updateSolr.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Author: seeker {2014-04-08 2:22:36}
	 * @Version：
	 * @Title: saveHBase
	 * @Description:
	 * 
	 * @param tablename
	 * @param put
	 * @throws IOException
	 */
	public void saveHBase(byte[] tablename, Put put) throws IOException {

		HTableInterface hti = pool.getTable(tablename);
		try {
			hti.put(put);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			hti.close();
		}
	}

	/**
	 * @Author: seeker {2014-04-08 2:22:42}
	 * @Version：
	 * @Title: saveSolr
	 * @Description:
	 * 
	 * @param sid
	 */
	public void saveSolr(SolrInputDocument sid) {

		try {
			updateSolr.add(sid);
			updateSolr.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Author: seeker {2014-04-08 2:22:51}
	 * @Version：
	 * @Title: updateSolr
	 * @Description:
	 * 
	 * @param sid
	 */
	public void updateSolr(SolrInputDocument sid) {

		try {
			updateSolr.add(sid);
			updateSolr.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Author: seeker {2014-04-08 2:22:58}
	 * @Version：
	 * @Title: findBySolr
	 * @Description:
	 * 
	 * @param idField
	 * @param filters
	 * @param field
	 * @param order
	 * @return
	 */
	public SolrDocumentList findBySolr(String idField, String[] filters, String field, SolrQuery.ORDER order) {

		QueryResponse response = null;
		try {
			SolrQuery query = new SolrQuery();
			query.setFields(idField);
			query.addFilterQuery(filters);
			query.addSort(field, order);

			response = querySolr.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
			return null;
		}
		return response.getResults();
	}

	/**
	 * @Author: seeker {2014-04-08 10:21:27}
	 * @Version：
	 * @Title: pageBySolr
	 * @Description:
	 * 
	 * @param idField
	 * @param filters
	 * @param field
	 * @param order
	 * @param page
	 * @return
	 */
	public SolrDocumentList pageBySolr(String idField, String[] filters, String field, SolrQuery.ORDER order, Page<?> page) {

		QueryResponse response = null;
		try {
			SolrQuery query = new SolrQuery();
			query.setFields(idField);
			query.addFilterQuery(filters);
			query.addSort(field, order);
			query.setStart(page.getCurrent() * page.getPageSize());
			query.setRows(page.getPageSize());

			response = querySolr.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
			return null;
		}
		page.setRecordCount((int) response.getResults().getNumFound());
		return response.getResults();
	}
}
