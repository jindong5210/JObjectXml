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
 * XML file writer
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
	 * Write java bean list to XML folder.<br/>
	 * Data file format : data_i.xml
	 * 
	 * @param folder
	 * @param ddoList
	 * @throws Exception
	 */
	public void writeXmlFolder(File folder , List<?> ddoList)
	throws Exception{
		writeXmlFolder(folder, ddoList,"UTF-8");
	}
	
	/**
	 * Write java bean list to XML folder by default encoding.<br/>
	 * Data file format : data_i.xml
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
	 * Write java bean to a XML file by default encoding.
	 * 
	 * @param xmlFile
	 * @param xmlDdo
	 * @throws Exception
	 */
	public void writeXml(File xmlFile , Object xmlDdo)
	throws Exception{
		writeXml(xmlFile, xmlDdo, "UTF-8");
	}
	
	/**
	 * Write java bean to a XML file.
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
	 * Write XML document to a XML file.
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
	 * Convert a java bean to XML document by default encoding.
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
