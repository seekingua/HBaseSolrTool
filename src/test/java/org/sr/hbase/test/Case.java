package org.sr.hbase.test;

import org.sr.hbase.annotation.HColumn;
import org.sr.hbase.annotation.HTable;

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

	public Case() {
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getType3() {
		return type3;
	}

	public void setType3(String type3) {
		this.type3 = type3;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString() {

		return id + "	" + code + "	" + type1 + "	" + type2 + "	" + type3 + "	" + date;
	}
}