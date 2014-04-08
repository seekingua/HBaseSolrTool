package com.inspur.hbase.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: seeker {2014-04-08 11:28:28}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: HTable
 * @Description: annotation defined for bean which associated with hbase meta data
 * 
 * 
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface HTable {

	String table() default ""; // hbase table name, if "", the default value is class simple name
}
