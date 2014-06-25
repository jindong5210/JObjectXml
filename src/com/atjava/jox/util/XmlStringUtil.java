package com.atjava.jox.util;

public class XmlStringUtil {

	public static String ReplaceUnicode(String value,char replacement) {
		char [] charArr = value.toCharArray();
	    for ( int i = 0; i < charArr.length; i++ ) {
	        if (charArr[i] > 0xFFFD) {
	        	charArr[i] = replacement;
	        } 
	        else if (charArr[i] < 0x20 && charArr[i] != '\t' & charArr[i] != '\n' & charArr[i] != '\r'){
	        	charArr[i] = replacement;
	        }
	    }
	    return new String(charArr);
	}
	
	public static String ReplaceUnicode(String value) {
		return ReplaceUnicode(value,' ');
	}
	
	public static void main(String[] args) {
		String s = "&nbsp";
		System.out.println(ReplaceUnicode(s));
	}
}
