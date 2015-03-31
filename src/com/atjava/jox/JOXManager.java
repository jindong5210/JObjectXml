//$Id: JOXManager.java,v 1.1 2009-12-7 kyo Exp $
/******************************************************************************
 * Created on 2009-12-7
 * Copyright(C) by www.atjava.com
 *
 * 2009-2-19      kyo
 *
 ******************************************************************************/
package com.atjava.jox;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.atjava.jox.annotation.ClassProps;
import com.atjava.jox.annotation.FieldProps;
import com.atjava.jox.annotation.FieldProps.ElementType;
import com.atjava.jox.convert.Converter;
import com.atjava.jox.convert.XmlDefaultConverter;

/**
 * XML file manager
 * @author  kyo
 */
public class JOXManager {
	
	private enum CollType{Set,List};
	
	private Converter converter;
	
	public JOXManager(Converter converter){
		this.converter = converter;
	}
	
	public JOXManager(){
		this.converter = new XmlDefaultConverter();
	}
	
	private Set<Field> getClsFields(Class<?> cls){
		//field set
		Set<Field> set = new HashSet<Field>();
		//add fields to set
		Field [] fields = cls.getDeclaredFields();
		if(fields == null || fields.length == 0)
			return set;
		for(int i=0;i<fields.length;i++){
			FieldProps anno = fields[i].getAnnotation(FieldProps.class);
			if(anno != null)
				set.add(fields[i]);
		}
		return set;
	}
	
	private void addCollectionElements(Element root,Object collection,String eleName,Field field)throws Exception{
		Element collEle = null;
		FieldProps property = field.getAnnotation(FieldProps.class);
		if(ElementType.ELEMENTLIST.equals(property.type()))
			collEle = root;
		else if(ElementType.ELEMENT.equals(property.type()))
			collEle = new DefaultElement(eleName);
		//Iterable
		if(collection instanceof Collection<?>){
			Iterable<?> coll = (Iterable<?>)collection;
			Iterator<?> iter = coll.iterator();
			while(iter.hasNext()){
				Object obj = iter.next();
				if(obj instanceof String && "".equals(property.strAlias()))
					throw new Exception("Collection type List<String> on field [ " + field.getName()
							+ " ] needs a annotation named strAlias !");
				
				Element ele = getElement(obj,property.strAlias(),null);
				collEle.add(ele);
			}
		}
		if(ElementType.ELEMENT.equals(property.type()))
			root.add(collEle);
	}
	
	private Object getCollectionObjects(Element root,Field field,String eleName)throws Exception{
		Collection<Object> o = null;
		FieldProps property = field.getAnnotation(FieldProps.class);
		
		//list generic type
		Class<?> type = getGenericCls(field);
		
		String cname = null;
		if(type.equals(String.class)){
			if("".equals(property.strAlias()))
				throw new Exception("Collection type List<String> on field [ " + field.getName() + " ] needs a annotation named strAlias !");
			cname = property.strAlias();
		}else{
			if(ElementType.ELEMENTLIST.equals(property.type()))
				cname = getClassAlias(type,null);
			else
				cname = getClassAlias(type,null);
		}
		
		Element collEle = root;
		if(ElementType.ELEMENTLIST.equals(property.type()))
			collEle = root;
		else if(ElementType.ELEMENT.equals(property.type()))
			collEle = (Element)root.selectSingleNode(eleName);
		
		if(collEle == null)
			return null;
		
		String tname = field.getType().getName();
		if(tname.startsWith("java.util") && tname.endsWith(CollType.List.toString())){
			o = new ArrayList<Object>();
		}else if(tname.startsWith("java.util") && tname.endsWith(CollType.Set.toString())){
			o = new HashSet<Object>();
		}
		
		//xml list elements
		List<?> childEles = collEle.selectNodes(cname);
		
		for(int i=0;i<childEles.size();i++){
			Element child = (Element)childEles.get(i);
			Object bean = null;
			if(type.equals(String.class)){
				bean = child.getText();
			}else{
				bean = getBean(child, type);
			}
			o.add(bean);
		}
		return o;
	}
	
	public void addSubObject(Object bean,Element root,Class<?> type)throws Exception{
		if(type.getName().equals("java.lang.Object"))
			return ;
		Set<Field> afields = getClsFields(type);
		Iterator<Field> fIt = afields.iterator();
		while(fIt.hasNext()){
			//Annotation field
			Field field = fIt.next();
			String fname = field.getName();
			Object fvalue = null;
			//Get field annotation
			FieldProps property = field.getAnnotation(FieldProps.class);
			if(!"".equals(property.alias()))
				fname = property.alias();
			//Annotation field set method
			Method setMethod = null;
			//Collection fields
			if(isCollectType(field)){
				fvalue = getCollectionObjects(root,field,fname);
			//Common fields
			}else if(isNotBasicType(field.getType())){
				Element notBasic = (Element)root.selectSingleNode(fname);
				if(notBasic != null)
					fvalue = getBean(notBasic, field.getType(), fname);
			}else{
				String svalue = "";
				// output type
				if(ElementType.ATTRIBUTE.equals(property.type())){
					Attribute attr = root.attribute(fname);
					if(attr == null) 
						svalue = null;
					else
						svalue = attr.getValue();
				}else if(ElementType.ELEMENT.equals(property.type())){
					Element element = (Element)root.selectSingleNode(fname);
					if(element == null) 
						svalue = null;
					else
						svalue = element.getText();
				}
				fvalue = converter.convert(field.getType(),svalue);
			}
			setMethod = type.getDeclaredMethod("set" + fstCharUpperCase(field.getName()), field.getType());
			if(type.getName().equals("java.lang.Object") && setMethod == null)
				throw new Exception("Field [ " + fname + " ] is no setter delared!");
			else if(setMethod == null){
				//Super class
				addSubObject(bean,root,type.getSuperclass());
			}
			//invoke set method
			if(fvalue != null)
				setMethod.invoke(bean, fvalue);
			//Super classes
			addSubObject(bean,root,type.getSuperclass());
		}
	}
	
	public void addSubElements(Object bean,Element root,Class<?> type)throws Exception{
		if(type.getName().equals("java.lang.Object"))
			return ;
		//Annotation fields
		Set<Field> afields = getClsFields(type);
		Iterator<Field> fIt = afields.iterator();
		while(fIt.hasNext()){
			//Annotation field
			Field field = fIt.next();
			String fname = field.getName();
			Object fvalue = null;
			//Annotation field get method
			Method getMethod = type.getDeclaredMethod("get" + fstCharUpperCase(fname));
			if(getMethod == null)
				throw new Exception("Field [ " + fname + " ] is no getter delared!");
			//invoke get method
			fvalue = getMethod.invoke(bean);
			if(fvalue == null)
				continue;
			//Get field annotation
			FieldProps property = field.getAnnotation(FieldProps.class);
			if(!"".equals(property.alias()))
				fname = property.alias();
			
			//Collection fields
			if(isCollectType(field)){
				addCollectionElements
				(root,fvalue, fname,field);
			//Common fields
			}else if(isNotBasicType(field.getType())){
				root.add(getElement(fvalue,fname));
			}else{
				// output type
				if(ElementType.ATTRIBUTE.equals(property.type())){
					root.addAttribute(fname, converter.convert(fvalue));
				}else if(ElementType.ELEMENT.equals(property.type())){
					Element fieldEle = new DefaultElement(fname);
					String strValue = converter.convert(fvalue);
					addText(fieldEle, strValue);
					root.add(fieldEle);
				}
			}
		}
		//Super classes
		addSubElements(bean,root,type.getSuperclass());
	}
	
	private boolean isNotBasicType(Class<?> cls){
		return !cls.isPrimitive() && !cls.getName().startsWith("java.lang");
	}
	
	private void addText(Element e,String strValue){
		if(strValue.indexOf("<") > 0 || strValue.indexOf(">") > 0 || strValue.indexOf("&") > 0)
			e.addCDATA(strValue);
		else
			e.addText(strValue);
	}
	
	protected boolean isCollectType(Field field){
		String tname = field.getType().getName();
		return 
				tname.startsWith("java.util.") 
			&& 
				(tname.endsWith(CollType.List.toString()) ||
				 tname.endsWith(CollType.Set.toString()));
	}
	
	public Element getElement(Object bean)throws Exception{
		return getElement(bean,null,null);
	}
	
	public Element getElement(Object bean,String fieldAlias)throws Exception{
		return getElement(bean,null,fieldAlias);
	}
	
	private Element getElement(Object bean,String strName,String fieldAlias)throws Exception{
		if(bean instanceof String){
			Element strEle = new DefaultElement(strName);
			addText(strEle,(String)bean);
			return strEle;
		}
		Class<?> cls = bean.getClass();
		String cname = getClassAlias(cls, fieldAlias);
		
		//Root element
		Element root = new DefaultElement(cname);
		addSubElements(bean, root, cls);
		return root;
	}
	
	public <T> T getBean(Element root, Class<T> cls)throws Exception{
		return getBean(root, cls, null);
	}
	
	public <T> T getBean(Element root, Class<T> cls,String fieldAlias)throws Exception{
		T bean = null;
		String cname = getClassAlias(cls, fieldAlias);
		Element parent = root.getParent();
		
		if(parent == null){
			bean = cls.newInstance();
		}else if (parent!= null && parent.selectNodes(cname).size() > 0) {
			bean = cls.newInstance();
		}
		if(bean != null)
			addSubObject(bean, root,cls);
		return bean;
	}
	
	private String getClassAlias(Class<?> cls,String fieldAlias){
		String cname = null;
		//Class Annotation
		if(StringUtils.isBlank(fieldAlias)){
			ClassProps cprops = cls.getAnnotation(ClassProps.class);
			if(cprops!= null && !"".equals(cprops.alias()))
				cname = cprops.alias();
			else
				cname = cls.getSimpleName();
		}else
			cname = fieldAlias;
		return cname;
	}
	
	
	public static String fstCharUpperCase(String str){
		if(str == null || "".equals(str))
			return "";
		return Character.toUpperCase
		(str.charAt(0)) + str.substring(1, str.length());
	}
	
	/**
	 * Get the generic class of the field
	 * @param field
	 * @return Class
	 * @throws Exception
	 */
	public Class<?> getGenericCls(Field field)throws Exception{
		ParameterizedType pt = (ParameterizedType)field.getGenericType();
		Type [] types = pt.getActualTypeArguments();
		if(types == null || types.length == 0)
			throw new Exception("the field [ " + field.getName() 
			+ " ] of the class [ " + field.getClass().toString() + " ] must be generic type");
		Class<?> genericCls = Class.forName
		(types[0].toString().replace("class ", ""));
		return genericCls;
	}
	
	public Converter getConverter() {
		return converter;
	}

	public void setConverter(Converter converter) {
		this.converter = converter;
	}
}
