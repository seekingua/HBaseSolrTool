HBaseSolrTool
=============

###1、	Demo示例
	
	在目录“src/test/java/com/inspur/hbase/test”中.
	

###2、	架构说明
	
![](https://github.com/SeekerResource/HBaseSolrTool/raw/master/docs/architecture.png)
	
###3、	注解说明
	
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
##4、	API说明
		
![Image text](https://github.com/SeekerResource/HBaseSolrTool/raw/master/docs/api_1.png)
![Image text](https://github.com/SeekerResource/HBaseSolrTool/raw/master/docs/api_2.png)

	
