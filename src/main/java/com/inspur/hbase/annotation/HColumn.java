package com.inspur.hbase.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhouzhd {2014-04-08 11:28:37}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: HColumn
 * @Description: annotation defined for bean field which associated with hbase meta data
 * 
 * 
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface HColumn {

	boolean id() default false; // means row key, only one field can be annotated by id

	boolean index() default false; // whether the solr index or not

	String family() default ""; // family value for field, if "", the default value for this field is field simple name

	String qualifier() default ""; // qualifier value for field, if "", the default value for this field is field simple name
}
