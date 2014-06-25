package com.atjava.jox.convert;

public interface Converter {

	public Object convert(Class<?> type,String s)throws Exception;
	
	public String convert(Object o);
	
}
