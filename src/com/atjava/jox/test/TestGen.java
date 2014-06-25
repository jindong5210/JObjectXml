package com.atjava.jox.test;

import com.atjava.jox.annotation.ClassProps;
import com.atjava.jox.annotation.FieldProps;
import com.atjava.jox.annotation.FieldProps.ElementType;

@ClassProps(alias = "test-gen")
public class TestGen extends TestSuperBean {

	@FieldProps(type = ElementType.ELEMENT)
	private String s1;
	
	@FieldProps(type = ElementType.ELEMENT)
	private String s2;
	
	@FieldProps
	private String s3;

	public String getS3() {
		return s3;
	}

	public void setS3(String s3) {
		this.s3 = s3;
	}

	public String getS1() {
		return s1;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String getS2() {
		return s2;
	}

	public void setS2(String s2) {
		this.s2 = s2;
	}
	
}
