HBaseSolrTool
=============

    HBaseSolrTool目的是解决HBase无法满足综合查询的要求，因此借用Solr的查询功能，结合HBase满足运行在HBase上应用对于多条件综合查询的要求。


###1、	说明
	
	
	
	需要注意问题：
	1)	搭建Solr环境
	2)	替换hbase-site.xml文件;
	
###2、	Demo示例
	
	在目录“src/test/java/com/inspur/hbase/test”中.
	

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
