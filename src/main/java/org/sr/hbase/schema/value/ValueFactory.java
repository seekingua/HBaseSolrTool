package org.sr.hbase.schema.value;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * @Organ: Inspur Group
 * @Teams: Big Data Team
 * @Author: zhouzhd {2014-03-27 11:39:01}
 * @Mail: zzd338@163.com
 * 
 * @ClassName: ValueFactory
 * @Description:
 * 
 * 
 */
public class ValueFactory {

	static Class<Integer> INTEGER = Integer.class;
	static Class<Double> DOUBLE = Double.class;
	static Class<Float> FLOAT = Float.class;
	static Class<String> STRING = String.class;

	/**
	 * @Author: zhouzhd {2014-03-28 6:14:32}
	 * @Version：
	 * @Title: Create
	 * @Description:
	 * 
	 * @param instance
	 * @return
	 */
	public static <T> Value Create(T instance) {

		if (instance == null) {
			return new NullValue();
		}
		Class<? extends Object> instanceClass = instance.getClass();
		if (instanceClass.equals(INTEGER)) {
			return new IntValue((Integer) instance);
		} else if (instanceClass.equals(DOUBLE)) {
			return new DoubleValue((Double) instance);
		} else if (instanceClass.equals(FLOAT)) {
			return new FloatValue((Float) instance);
		} else if (instanceClass.equals(STRING)) {
			return new StrValue((String) instance);
		} else {
			return new StrValue((String) instance.toString());
		}
	}

	/**
	 * @Author: zhouzhd {2014-03-28 6:14:17}
	 * @Version：
	 * @Title: CreateObject
	 * @Description:
	 * 
	 * @param clazz
	 * @param bytes
	 * @return
	 */
	public static Object CreateObject(Class<?> clazz, byte[] bytes) {

		if (clazz.equals(int.class)) {
			return new Integer(Bytes.toInt(bytes));
		} else if (clazz.equals(double.class)) {
			return new Double(Bytes.toDouble(bytes));
		} else if (clazz.equals(float.class)) {
			return new Float(Bytes.toFloat(bytes));
		} else if (clazz.equals(INTEGER)) {
			return new Integer(Bytes.toInt(bytes));
		} else if (clazz.equals(DOUBLE)) {
			return new Double(Bytes.toDouble(bytes));
		} else if (clazz.equals(FLOAT)) {
			return new Float(Bytes.toFloat(bytes));
		} else if (clazz.equals(STRING)) {
			return new String(Bytes.toString(bytes));
		} else {
			return null;
		}
	}

	/**
	 * @Author: zhouzhd {2014-03-28 6:14:25}
	 * @Version：
	 * @Title: TypeCreate
	 * @Description:
	 * 
	 * @param value
	 * @return
	 */
	public static Value TypeCreate(String value) {

		return new StrValue(value);
	}
}
