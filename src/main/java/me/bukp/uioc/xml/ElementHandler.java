package me.bukp.uioc.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.bukp.uioc.exception.XmlParseException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public abstract class ElementHandler {
	
	protected List<Document> getDocument(String... filePaths) {
		SAXReader reader = new SAXReader();
		List<Document> docs = new ArrayList<>();
		try {
			for (String file : filePaths) {
				File xmlFile = new File(file);
				docs.add(reader.read(xmlFile));
			}
		} catch (DocumentException ex) {
			throw new XmlParseException(ex.getMessage());
		}
		return docs;
	}
	
	protected String getElementAttributeValue(Element e, String attr) {
		return e.attributeValue(attr);
	}
}
