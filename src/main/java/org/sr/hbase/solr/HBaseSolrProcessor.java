package org.sr.hbase.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.EntityProcessorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhouzhd {2014-04-08 3:05:12}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: HBaseSolrProcessor
 * @Description: for hbase data handling, this class needs to be copied to the specified directory(solr's "WEB-INF/classes" with full package path)
 * 
 * 
 */
public class HBaseSolrProcessor extends EntityProcessorBase {

	private static final Logger LOG = LoggerFactory.getLogger(HBaseSolrProcessor.class);
	private HTableInterface table;
	private Iterator<Result> resultIterator;
	private ResultScanner resultScanner;
	private String tableName;
	private Configuration hbaseConfig;

	@Override
	public void init(Context context) {

		super.init(context);
		tableName = context.getEntityAttribute("tableName");
		hbaseConfig = HBaseConfiguration.create();
		HTablePool htp = new HTablePool(hbaseConfig, 500);
		table = htp.getTable(tableName);

		Scan scan = new Scan();
		try {
			resultScanner = table.getScanner(scan);
			List<Result> resultList = new ArrayList<Result>();
			for (Result rs : resultScanner) {
				resultList.add(rs);
			}
			resultIterator = resultList.iterator();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
				htp.close();
				LOG.info("finished with no error!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Map<String, Object> nextRow() {

		if (resultIterator.hasNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			Result result = resultIterator.next();
			map.put("rowdata", result);
			map.put("hbaseConfig", hbaseConfig);
			return map;
		}
		return null;
	}
}
