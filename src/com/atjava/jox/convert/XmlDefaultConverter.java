package com.atjava.jox.convert;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.atjava.jox.JOXManager;
import com.atjava.jox.util.XmlStringUtil;

public class XmlDefaultConverter implements Converter {
	
	private DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public Object convert(Class<?> type,String s) throws Exception {
		
		if(s == null || "null".equals(s))
			return null;

		Object value = s;
		String tname = type.getName();
		
		if("java.util.Date".equals(tname)){
			value = df.parse(s);
		}else if((tname.startsWith("java.lang") && !tname.equals("java.lang.String")) ||
				 type.isPrimitive()){
			//Integer 
			if(tname.equals("java.lang.Integer") || tname.equals("int")){
				value = Integer.parseInt(s);
			//Character
			}else if(tname.equals("java.lang.Character") || tname.equals("char")){
				value = s.charAt(0);
			//Other primitive type
			}else{
				Class<?> packClass = type;
				//Primitive class
				if(!tname.startsWith("java.lang"))
					packClass = Class.forName("java.lang." + JOXManager.fstCharUpperCase(tname));
				//Get parse method
				Method parseMethod = packClass.getMethod("parse" + JOXManager.fstCharUpperCase(tname), String.class);
				value = parseMethod.invoke(type, s);
			}
		}else if(tname.equals("java.lang.String")){
			//value = s.replaceAll("]]>","] ] >");
			//System.out.println(s.indexOf("]]>"));
		}else{
			throw new Exception("Unsupport data type [ " + type + " ]");
		}
		return value;
	}

	public String convert(Object o) {
		
		if(o instanceof Date)
			return df.format((Date)o);
		else{
			return XmlStringUtil.ReplaceUnicode(String.valueOf(o))
			.replaceAll("]]>","] ] >");
		}
	}
	
}
