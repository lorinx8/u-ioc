package me.bukp.uioc.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.bukp.uioc.common.Constants;

import org.dom4j.Document;
import org.dom4j.Element;

public class BeanElementHandler extends ElementHandler {
	
	protected Map<String, Element> beanElmentsMap = new HashMap<>();
	
	public BeanElementHandler() {}
	
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
			beanElmentsMap.put(getElementAttributeValue(e, Constants.BEAN_PROPERTY_ID), e);
		}
	}
	
	/**
	 * 判读Bean是否延迟加载，若延迟加载则返回true，否则返回false
	 * @param e bean元素对象
	 * @return Bean元素是否延迟加载
	 */
	public boolean isLazy(Element e) {
		boolean lazy = Boolean.getBoolean(getElementAttributeValue(e, Constants.BEAN_PROPERTY_LAZY_INIT));
		Element p = e.getParent();
		boolean pLzay = Boolean.getBoolean(getElementAttributeValue(p, Constants.BEANS_PROPERTY_DEFAULT_LAZY_INIT));
		if (lazy || pLzay) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断Bean是否单例，若是单例则返回true，否则返回false
	 * @param e bean元素对象
	 * @return Bean元素是否单例
	 */
	public boolean isSingleton(Element e) {
		return Boolean.getBoolean(getElementAttributeValue(e, Constants.BEAN_PROPERTY_SINGLETON));
	}
	
	/**
	 * 得到Bean元素节点中，构造函数节点元素集合
	 * @param e bean元素对象
	 * @return 构造函数节点元素集合
	 */
	public List<Element> getConstructorElements(Element e) {
		return getSubElementsByName(e, Constants.BEAN_ELEMENT_CONSTRUCTOR_ARG);
	}

	/**
	 * 得到Bean元素节点中，属性节点元素集合
	 * @param e bean元素对象
	 * @return 属性节点元素集合
	 */
	public List<Element> getPropertyrElements(Element e) {
		return getSubElementsByName(e, Constants.BEAN_ELEMENT_PROPERTY);
	}
}
