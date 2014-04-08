package com.inspur.hbase.solr;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.Transformer;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: seeker {2014-04-08 3:05:53}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: HBaseSolrTransformer
 * @Description: for hbase data handling, this class needs to be copied to the specified directory(solr's "WEB-INF/classes" with full package path)
 * 
 * 
 */
public class HBaseSolrTransformer extends Transformer {

	@Override
	public Object transformRow(Map<String, Object> row, Context context) {

		Map<String, Object> retMap = new HashMap<String, Object>();
		String cf = context.getEntityAttribute("family");
		if (null != row && row.get("rowdata") != null) {
			Result result = (Result) row.get("rowdata");
			for (Map<String, String> field : context.getAllEntityFields()) {
				if (field.get("name").equals("id")) {
					retMap.put("id", Bytes.toString(result.getRow()));
				} else {
					retMap.put(field.get("name"), Bytes.toString(result.getValue(Bytes.toBytes(cf), Bytes.toBytes(field.get("column")))));
				}
			}
		}
		return retMap;
	}
}
