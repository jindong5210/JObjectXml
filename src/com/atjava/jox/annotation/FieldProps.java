package com.atjava.jox.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)   
@Documented
@Inherited 
public @interface FieldProps {

	public enum ElementType {

	    /** Xml tags */
	    ELEMENT,
	    
	    /** Xml attribute */
	    ATTRIBUTE,
	    
	    /** Xml tags list*/
	    ELEMENTLIST,
	    
	}
	
	public ElementType type() default ElementType.ATTRIBUTE;
	
	public String alias() default "";
	
	public String strAlias() default "";
	
}
