package com.atjava.jox.test;

import java.util.Date;
import java.util.List;

import com.atjava.jox.annotation.ClassProps;
import com.atjava.jox.annotation.FieldProps;
import com.atjava.jox.annotation.FieldProps.ElementType;

@ClassProps(alias = "test-bean")
public class TestBean extends TestSuperBean{
	
	@FieldProps
	private int intAttr;
	
	@FieldProps(type = ElementType.ELEMENT,alias = "list-element1")
	private List<TestGen> listElmt1;
	
	@FieldProps(type = ElementType.ELEMENTLIST,alias = "list-element2")
	private List<TestSuperBean> listElmt2;
	
	@FieldProps(alias = "xmlns1")
	private String strAttr;
	
	@FieldProps(type = ElementType.ELEMENT)
	private TestGen testGen;
	
	@FieldProps
	private Date dateAttr;
	
	@FieldProps(type = ElementType.ELEMENT)
	private String strElmt;
	
	@FieldProps(type = ElementType.ELEMENTLIST,alias = "str-list",strAlias = "string")
	private List<String> strList;
	
	@FieldProps(type = ElementType.ELEMENTLIST,alias = "str-list1",strAlias = "string1")
	private List<String> strList1;
	
	public TestGen getTestGen() {
		return testGen;
	}

	public void setTestGen(TestGen testGen) {
		this.testGen = testGen;
	}

	public List<String> getStrList1() {
		return strList1;
	}

	public void setStrList1(List<String> strList1) {
		this.strList1 = strList1;
	}

	public List<String> getStrList() {
		return strList;
	}

	public void setStrList(List<String> strList) {
		this.strList = strList;
	}

	public int getIntAttr() {
		return intAttr;
	}

	public void setIntAttr(int intAttr) {
		this.intAttr = intAttr;
	}

	public List<TestGen> getListElmt1() {
		return listElmt1;
	}

	public void setListElmt1(List<TestGen> listElmt1) {
		this.listElmt1 = listElmt1;
	}

	public List<TestSuperBean> getListElmt2() {
		return listElmt2;
	}

	public void setListElmt2(List<TestSuperBean> listElmt2) {
		this.listElmt2 = listElmt2;
	}

	public String getStrAttr() {
		return strAttr;
	}

	public void setStrAttr(String strAttr) {
		this.strAttr = strAttr;
	}

	public Date getDateAttr() {
		return dateAttr;
	}

	public void setDateAttr(Date dateAttr) {
		this.dateAttr = dateAttr;
	}

	public String getStrElmt() {
		return strElmt;
	}

	public void setStrElmt(String strElmt) {
		this.strElmt = strElmt;
	}
	

}
