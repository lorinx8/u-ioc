package me.bukp.uioc.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.bukp.uioc.exception.XmlParseException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 对xml文件及其元素进行处理的抽象类
 * 2012-12-12
 */
public abstract class ElementHandler {
	
	/**
	 * 根据路径得到文档对象
	 * @param filePaths xml文件路径
	 * @return xml文档对象集合
	 */
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
	
	/**
	 * 根据元素属性得到值
	 * @param e 元素对象
	 * @param attr 属性名
	 * @return 属性值
	 */
	protected String getElementAttributeValue(Element e, String attr) {
		return e.attributeValue(attr);
	}
	
	/**
	 * 根据元素名得到元素的子元素集合
	 * @param e 元素对象
	 * @param name 子元素名
	 * @return 子元素集合
	 */
	protected List<Element> getSubElementsByName(Element e, String name) {
		List<Element> subElements = e.elements();
		List<Element> matchedElements = new ArrayList<>();
		for (Element element : subElements) {
			if (element.getName().equals(name)) {
				matchedElements.add(element);
			}
		}
		return matchedElements;
	}
}
