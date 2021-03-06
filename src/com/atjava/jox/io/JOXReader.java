//$Id: JOXReader.java,v 1.1 2009-12-7 kyo Exp $
/******************************************************************************
 * Created on 2009-12-7
 * Copyright(C) by www.atjava.com
 *
 * 2009-2-19      kyo
 *
 ******************************************************************************/
package com.atjava.jox.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;

import com.atjava.jox.JOXManager;
import com.atjava.jox.convert.Converter;
import com.atjava.jox.convert.XmlDefaultConverter;

/**
 * XML file reader
 * @author kyo
 *
 */
public class JOXReader {
	
	private Converter converter;
	
	public JOXReader(Converter converter){
		this.converter = converter;
	}
	
	public JOXReader(){
		this.converter = new XmlDefaultConverter();
	}

	/**
	 * Read a java bean Object from a XML file
	 * @param <T>
	 * @param f
	 * @param cls
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public <T> T readXml(File f , Class<T> cls, String encoding)throws Exception{
		
		if(!f.isFile()){
			return null;
		}
		T obj = null;
		SAXReader reader = new SAXReader();
		reader.setEncoding(encoding);
		
		JOXManager mgr = new JOXManager();
		if(converter != null)
			mgr.setConverter(converter);
		
		Document doc = reader.read(f);
		
		obj = (T)mgr.getBean(doc.getRootElement(),cls);
			
		return obj;
		
	}
	
	/**
	 * Read a java bean Object collection from a XML folder
	 * @param folder
	 * @param cls
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public List<Object> readXmlFolder(File folder , Class<?> cls,String encoding)throws Exception{
		if(!folder.isDirectory()){
			return null;
		}
		List<Object> list = new ArrayList<Object>();
		SAXReader reader = new SAXReader();
		reader.setEncoding(encoding);
		
		JOXManager mgr = new JOXManager();
		if(converter != null)
			mgr.setConverter(converter);
		
		File [] fs = folder.listFiles();
		for (int i = 0; i < fs.length; i++) {
			String extend = fs[i].getName().substring(fs[i].getName().lastIndexOf(".")+1, fs[i].getName().length());
			if(!"xml".equalsIgnoreCase(extend))
				continue;
			
			Document doc = reader.read(fs[i]);
			
			Object obj = mgr.getBean(doc.getRootElement(),cls);
			list.add(obj);
		}
		return list;
	}
	
	/**
	 * Read a java bean Object from a XML file by default encoding
	 * @param <T>
	 * @param f
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> T readXml(File f , Class<T> cls)throws Exception{
		return readXml(f, cls, "UTF-8");
	}
	
	/**
	 * Read XML document from a XML file
	 * @param <T>
	 * @param f
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public Document readXml(File f,String encoding)throws Exception{
		SAXReader reader = new SAXReader();
		reader.setEncoding(encoding);
		return reader.read(f);
	}
	
	/**
	 * Read XML document from a String
	 * @param <T>
	 * @param f
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public Document readXml(File f)throws Exception{
		SAXReader reader = new SAXReader();
		reader.setEncoding("UTF-8");
		return reader.read(f);
	}
	
	/**
	 * Read a java bean Object from a XML String
	 * @param <T>
	 * @param f
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> T readXml(String xml , Class<T> cls)throws Exception{
		Document doc = DocumentHelper.parseText(xml);
		JOXManager mgr = new JOXManager();
		T obj = (T)mgr.getBean(doc.getRootElement(),cls);
		return obj;
	}
	
	/**
	 * Read a java bean Object collection from a XML folder by default encoding.
	 * @param folder
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public List<Object> readXmlFolder(File folder , Class<?> cls)throws Exception{
		return readXmlFolder(folder, cls, "UTF-8");
	}
}
