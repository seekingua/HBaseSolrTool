HBaseSolrTool
=============

HBaseSolrTool目的是解决目前HBase二级索引方案不够完善，无法满足HBase对于综合查询的需求。因此借用Solr的查询功能，满足运行在HBase上的应用对于多条件综合查询的需求。


###1、	使用说明
	
	
	1)	因为在HBaseSolrTransformer类中已经硬编码，java bean中必须有id属性，该属性将作为HBase的rowkey；
	2)	HBaseSolrTool-1.0-SNAPSHOT.jar需要放到solr的lib中；
	3)	替换hbase-site.xml文件为所使用集群的hbase-site.xml文件;
	
###2、	Demo示例
	
	在目录“src/test/java/org/sr/hbase/test”中.
	

###3、	架构说明
	
![](https://github.com/SeekerResource/HBaseSolrTool/raw/master/docs/architecture.png)
	
###4、	注解说明
	
####A、	HTable
	
		public @interface HTable {
		
			String table() default "";   // Hbase表设置，默认存储为类名    
		
		}															   
	
####B、	HColumn
	
		public @interface HColumn {	
		
			boolean id() default false;   // 是否作为HBase rowkey	
			
			boolean index() default false; // 是否是Solr索引	
			
			String family() default ""; // column family设置，默认存储为属性名  
			
			String qualifier() default ""; // qualifier设置，默认存储为属性名      
		} 																	  
###5、	API说明
		
![Image text](https://github.com/SeekerResource/HBaseSolrTool/raw/master/docs/api_1.png)
![Image text](https://github.com/SeekerResource/HBaseSolrTool/raw/master/docs/api_2.png)

	
###6、	示例中类Case说明

	@HTable(table = "Case")
	public class Case {
		@HColumn(id = true, index = true)
		private String id;
		@HColumn(family = "cf", index = true)
		private String code;
		@HColumn(family = "cf")
		private String type1;
		@HColumn(family = "cf")
		private String type2;
		@HColumn(family = "cf", index = true)
		private String type3;
		@HColumn(family = "cf", qualifier = "date_q", index = true)
		private String date;
		……
	}
	
	1)	@HTable(table = "Case")
		表注解，该含义是将类Case与HBase表Case关联；
	2)	@HColumn(id = true, index = true)
		列注解，含义是该属性将作为HBase rowkey，并且作为Solr的索引列；
	3)	@HColumn(family = "cf", qualifier = "date_q", index = true)
		列注解，含义是该属性对应HBase的column family为“cf”，qualifier为“date_q”，并且作为Solr的索引列；

###7、	solr配置示例
	
	1)	schema.xml
		……
		<fields>  
 			<field name="_version_" type="long" indexed="true" stored="true"/>  
		   	<field name="id" type="string" indexed="true" stored="true"  multiValued="false" />
		   	<field name="code" type="string" indexed="true" stored="true"  multiValued="false" /> 
		   	<field name="type1" type="string" indexed="true" stored="true" multiValued="false"/>
		   	<field name="type3" type="string" indexed="true" stored="true" multiValued="false"/>
		   	<field name="date" type="string" indexed="true" stored="true" multiValued="false"/>
 		</fields>
 		<uniqueKey>id</uniqueKey>
		<defaultSearchField>id</defaultSearchField>
		……
	2)	solrconfig.xml	
		……
		<requestHandler class="org.apache.solr.handler.dataimport.DataImportHandler" name="/dataimport">
			<lst name="defaults">
				<str name="config">di.xml</str>
			</lst>
		</requestHandler>	
		……
	3)	di.xml
		……
		<dataConfig>
			<document>
				<entity name="hbasedata" rootEntity="true" processor="org.sr.hbase.solr.HBaseSolrProcessor"
					transformer="DateFormatTransformer,RegexTransformer,TemplateTransformer,org.sr.hbase.solr.HBaseSolrTransformer,LogTransformer" 
					zookeeper="192.168.0.100,192.168.0.230,192.168.0.234" parent="/hbase" tableName="Case" family="cf" operateModel="add">
					<field column="id" name="id"/>
					<field column="code" name="code"/>
					<field column="type1" name="type1"/>
					<field column="type3" name="type3"/>
					<field column="date_q" name="date"/>
				</entity>
			</document>
		</dataConfig>
		……