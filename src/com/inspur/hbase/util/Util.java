package com.inspur.hbase.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhengde zhou {2014-03-27 10:46:22}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: util
 * @Description: utils for this tool
 * 
 * 
 */
public class Util {

	/**
	 * @Author: zhengde zhou {2014-04-08 3:11:50}
	 * @Version：
	 * @Title: findGetMethod
	 * @Description:
	 * 
	 * @param field
	 * @return
	 */
	public static Method findGetMethod(Field field) {

		String methodName = methodFromField(field, "get");
		Method fieldGetMethod;
		try {
			fieldGetMethod = field.getDeclaringClass().getMethod(methodName);
		} catch (Exception e) {
			return null;
		}
		if (fieldGetMethod.getReturnType() != field.getType()) {
			return null;
		}
		return fieldGetMethod;
	}

	/**
	 * @Author: zhengde zhou {2014-04-08 3:11:41}
	 * @Version：
	 * @Title: getFromField
	 * @Description:
	 * 
	 * @param instance
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static <T> Object getFromField(T instance, Field field) throws Exception {

		Method m = findGetMethod(field);
		if (m == null) {
			return null;
		}
		return m.invoke(instance);
	}

	/**
	 * @Author: zhengde zhou {2014-04-08 3:11:29}
	 * @Version：
	 * @Title: setToField
	 * @Description:
	 * 
	 * @param instance
	 * @param field
	 * @param value
	 * @throws Exception
	 */
	public static <T> void setToField(T instance, Field field, Object value) throws Exception {

		findSetMethod(field).invoke(instance, value);
	}

	/**
	 * @Author: zhengde zhou {2014-04-08 3:11:10}
	 * @Version：
	 * @Title: methodFromField
	 * @Description:
	 * 
	 * @param field
	 * @param prefix
	 * @return
	 */
	private static String methodFromField(Field field, String prefix) {

		return prefix + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
	}

	/**
	 * @Author: zhengde zhou {2014-04-08 3:11:00}
	 * @Version：
	 * @Title: findSetMethod
	 * @Description:
	 * 
	 * @param field
	 * @return
	 */
	public static Method findSetMethod(Field field) {

		String methodName = methodFromField(field, "set");
		Method fieldSetMethod = null;
		try {
			fieldSetMethod = field.getDeclaringClass().getMethod(methodName, field.getType());
		} catch (Exception e) {
			return null;
		}
		if (fieldSetMethod.getReturnType() != void.class) {
			return null;
		}
		return fieldSetMethod;
	}
}
