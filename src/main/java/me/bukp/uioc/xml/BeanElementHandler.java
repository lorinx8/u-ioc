package me.bukp.uioc.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import me.bukp.uioc.common.Constant;

import org.dom4j.Document;
import org.dom4j.Element;

public class BeanElementHandler extends ElementHandler {
	
	protected Map<String, Element> beanElmentsMap = new HashMap<>();
	
	public BeanElementHandler() {
	}
	
	/**
	 * 使用xml文件路径构造BeanElementHandler对象
	 * @param filePaths xml文件路径
	 * @exception XmlParseException
	 */
	public BeanElementHandler(String... filePaths) {
		List<Document> docs = getDocument(filePaths);
		for (int i = 0; i < docs.size(); i++) {
			addBeanElements(docs.get(i));
		}
	}
	
	/**
	 * 向BeanElementHandler实例中，添加xml文档对象中包含的Bean元素
	 * @param doc xml文档对象
	 */
	@SuppressWarnings("unchecked")
	private void addBeanElements(Document doc) {
		List<Element> es = doc.getRootElement().elements();
		for (Element e : es) {
			beanElmentsMap.put(getElementAttributeValue(e, Constant.BEAN_PROPERTY_ID), e);
		}
	}
	
	/**
	 * 判读Bean是否延迟加载，若延迟加载则返回true，否则返回false
	 * @param e bean元素对象
	 * @return Bean元素是否延迟加载
	 */
	public boolean isLazy(Element e) {
		boolean lazy = Boolean.getBoolean(getElementAttributeValue(e, Constant.BEAN_PROPERTY_LAZY_INIT));
		Element p = e.getParent();
		boolean pLzay = Boolean.getBoolean(getElementAttributeValue(p, Constant.BEANS_PROPERTY_DEFAULT_LAZY_INIT));
		if (lazy || pLzay) {
			return true;
		}
		return false;
	}
}
