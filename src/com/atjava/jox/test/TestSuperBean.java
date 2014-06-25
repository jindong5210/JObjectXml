package com.atjava.jox.test;

import java.util.List;

import com.atjava.jox.annotation.ClassProps;
import com.atjava.jox.annotation.FieldProps;
import com.atjava.jox.annotation.FieldProps.ElementType;

@ClassProps(alias = "test-super-bean")
public class TestSuperBean {

	@FieldProps(type = ElementType.ELEMENT,alias = "super-str")
	private String superStr;
	
	@FieldProps(type = ElementType.ELEMENTLIST,strAlias = "string2")
	private List<String> strings;

	public String getSuperStr() {
		return superStr;
	}

	public void setSuperStr(String superStr) {
		this.superStr = superStr;
	}

	public List<String> getStrings() {
		return strings;
	}

	public void setStrings(List<String> strings) {
		this.strings = strings;
	}
	
}
