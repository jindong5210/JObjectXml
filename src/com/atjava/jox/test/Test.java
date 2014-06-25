package com.atjava.jox.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.atjava.jox.io.JOXReader;
import com.atjava.jox.io.JOXWriter;

public class Test {

	public static void testRead()throws Exception{
		JOXReader reader = new JOXReader();
		System.out.println(reader.readXml(new File("c:/123.xml"), TestBean.class));
	}
	
	
	public static void testWrite()throws Exception{
		JOXWriter writer = new JOXWriter();
		TestBean tb = new TestBean();
		tb.setStrAttr("http://www.qingliu.com/QLight");
		tb.setStrElmt("sdfsdfsdfsdf");
		tb.setSuperStr("superString");
		tb.setDateAttr(new Date());
		
		List<TestGen> list1 = new ArrayList<TestGen>();
		
		TestGen gen11 = new TestGen();
		gen11.setS1("gen1-s1");gen11.setS2("gen1-s2");gen11.setS3("gen1-s3");
		gen11.setSuperStr("gen-super-s11");
		list1.add(gen11);
		
		TestGen gen12 = new TestGen();
		gen12.setS1("gen2-s1");gen12.setS2("gen2-s2");gen12.setS3("gen2-s3");
		list1.add(gen12);
		
		List<TestSuperBean> list2 = new ArrayList<TestSuperBean>();
		
		TestSuperBean gen21 = new TestSuperBean();
		gen21.setSuperStr("gen-super-s1");
		list2.add(gen21);
		
		TestSuperBean gen22 = new TestSuperBean();
		gen22.setSuperStr("gen-super-s2");
		list2.add(gen22);
		
		tb.setListElmt1(list1);
		tb.setListElmt2(list2);
		
		List<String> strList = new ArrayList<String>();
		strList.add("one");strList.add("two");strList.add("three");
		tb.setStrList(strList);
		
		strList = new ArrayList<String>();
		strList.add("one1");strList.add("two1");strList.add("three1");
		tb.setStrList1(strList);
		
		tb.setTestGen(gen12);
		
		gen12.setStrings(strList);
		//Document doc = writer.getDocument(tb,"utf-8");
		
		//System.out.println(doc.asXML());
		writer.writeXml(new File("/Users/jin-dong/123.xml"), tb);
		System.out.println(tb);
	}
	
	public static void main(String[] args)throws Exception {
		
		testRead();
		
		testWrite();
		
	}
}
