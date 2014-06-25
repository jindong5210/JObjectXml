package com.atjava.jox.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.atjava.jox.JOXManager;
import com.atjava.jox.convert.Converter;
import com.atjava.jox.convert.XmlDefaultConverter;

/**
 * XML文件写入
 * @author kyo
 *
 */
public class JOXWriter {
	
	private Converter converter;
	
	public JOXWriter(Converter converter){
		this.converter = converter;
	}
	
	public JOXWriter(){
		this.converter = new XmlDefaultConverter();
	}
	
	/**
	 * 将JavaBean集合写入文件夹
	 * 存储文件格式为data_i.xml,i从0开始递增。
	 * @param folder
	 * @param ddoList
	 * @throws Exception
	 */
	public void writeXmlFolder(File folder , List<?> ddoList)
	throws Exception{
		writeXmlFolder(folder, ddoList,"UTF-8");
	}
	
	/**
	 * 将JavaBean集合写入文件夹(用指定的编码) 
	 * 存储文件格式为data_i.xml,i从0开始递增。
	 * @param folder
	 * @param ddoList
	 * @param encoding
	 * @throws Exception
	 */
	public void writeXmlFolder(File folder , List<?> ddoList,String encoding)
	throws Exception{
		XMLWriter writer = null;
		JOXManager jox = new JOXManager();
		if(converter != null)
			jox.setConverter(converter);
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		try {
			DocumentFactory factory = DocumentFactory.getInstance();
			Document doc = factory.createDocument(encoding);
			
			for (int i = 0; i < ddoList.size(); i++) {
				
				String filePath = folder + "/data_" +i + ".xml";
				
				File f = new File(filePath);
				if(!f.exists())
					folder.mkdirs();
				
				if(f.createNewFile()){
					writer = new XMLWriter
					(new OutputStreamWriter(new FileOutputStream(f),encoding),format);
					
					Object ddo = ddoList.get(i);
					Element root = jox.getElement(ddo);
					doc.setRootElement(root);
					
					writer.write(doc);
					writer.close();
					writer = null;
				} 
			}
		} catch (Exception e) {
			throw e;
		} finally{
			if(writer != null)
				writer.close();
		}
	}
	
	/**
	 * 将一个指定的JavaBean对像保存为一个XML数据文件
	 * @param xmlFile
	 * @param xmlDdo
	 * @throws Exception
	 */
	public void writeXml(File xmlFile , Object xmlDdo)
	throws Exception{
		writeXml(xmlFile, xmlDdo, "UTF-8");
	}
	
	/**
	 * 将一个指定的JavaBean对像保存为一个XML数据文件(用指定的编码) 
	 * @param xmlFile
	 * @param xmlDdo
	 * @throws Exception
	 */
	public void writeXml(File xmlFile , Object xmlDdo,String encoding)
	throws Exception{
		Document doc = getDocument(xmlDdo, encoding);
		writeXml(xmlFile, doc , encoding);
	}
	
	/**
	 * 将一个指定的XMLDOM对像保存为一个XML数据文件(用指定的编码) 
	 * @param xmlFile
	 * @param xmlDdo
	 * @throws Exception
	 */
	public void writeXml(File xmlFile , Document doc,String encoding)
	throws Exception{
		XMLWriter writer = null;
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
	        writer = new XMLWriter(new OutputStreamWriter
	        (new FileOutputStream(xmlFile),encoding),format);
			writer.write(doc);
		} catch (Exception e) {
			throw e;
		} finally{
			if(writer != null)
				writer.close();
		}
	}
	
	/**
	 * 把一个JavaBean对像转换为XMLDOM对像(用指定的编码) 
	 * @param xmlFile
	 * @param xmlDdo
	 * @throws Exception
	 */
	public Document getDocument(Object xmlDdo,String encoding)
	throws Exception{
		JOXManager jox = new JOXManager();
		if(converter != null)
			jox.setConverter(converter);
		try {
			DocumentFactory factory = DocumentFactory.getInstance();
			Document doc = factory.createDocument(encoding);
			
			Element root = jox.getElement(xmlDdo);
			doc.setRootElement(root);
			
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Convert java bean to error!");
		}
	}

}
