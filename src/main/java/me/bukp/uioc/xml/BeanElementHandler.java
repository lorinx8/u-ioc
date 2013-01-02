package me.bukp.uioc.xml;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.bukp.uioc.common.Constants;
import me.bukp.uioc.domain.Autowire;
import me.bukp.uioc.domain.ByNameAutowire;
import me.bukp.uioc.domain.DataElement;
import me.bukp.uioc.domain.NoAutowire;
import me.bukp.uioc.domain.PropertyElement;
import me.bukp.uioc.domain.ReferElement;
import me.bukp.uioc.domain.ValueElement;
import me.bukp.uioc.exception.XmlParseException;

import org.dom4j.Document;
import org.dom4j.Element;

public class BeanElementHandler extends ElementHandler {
	
	/**
	 * bean id 与 bean元素的键值对集合
	 */
	protected Map<String, Element> beanElmentsMap = new HashMap<>();
	
	public BeanElementHandler() {}
	
	/**
	 * 使用xml文件路径构造BeanElementHandler对象
	 * @param filePaths xml文件路径
	 * @exception XmlParseException
	 */
	public BeanElementHandler(String... filePaths) {
		for (String file : filePaths) {
			addBeanElements(file);
		}
	}
	
	/**
	 * 向BeanElementHandler实例中，添加xml文档对象中包含的Bean元素
	 * @param path xml文件路径
	 */
	public void addBeanElements(String path) {
		List<Document> docs = getDocument(path);
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
	 * 根据bean id得到bean节点元素对象
	 * @param id bean id
	 * @return bean节点元素对象
	 */
	public Element getBeanElement(String id) {
		return beanElmentsMap.get(id);
	}
	
	/**
	 * 得到BeanElementHandler容器中的所有bena节点元素对象集合
	 * @return bena节点元素对象集合
	 */
	public Map<String, Element> getBeanElements() {
		return beanElmentsMap;
	}
	
	/**
	 * 根据bean元素对象得到其class属性
	 * @param e
	 * @return
	 */
	public String getBeanClassName(Element e) {
		return getElementAttributeValue(e, Constants.BEAN_PROPERTY_CLASS);
	}
	
	/**
	 * 判读Bean是否延迟加载，若延迟加载则返回true，否则返回false
	 * @param e bean元素对象
	 * @return Bean元素是否延迟加载
	 */
	public boolean isLazy(Element e) {
		//如果bean元素节点包含lazy-init属性
		String cLazy = this.getElementAttributeValue(e, Constants.BEAN_PROPERTY_LAZY_INIT);
		if (cLazy != null) {
			return Boolean.parseBoolean(cLazy);
		}
		
		//如果没有，则从beans节点寻找
		Element p = e.getParent();
		String  pLzay= getElementAttributeValue(p, Constants.BEANS_PROPERTY_DEFAULT_LAZY_INIT);
		if (pLzay != null) {
			return Boolean.parseBoolean(pLzay);
		}
		//默认为不懒加载
		return false;
	}
	
	/**
	 * 判断Bean是否单例，若是单例则返回true，否则返回false
	 * @param e bean元素对象
	 * @return Bean元素是否单例
	 */
	public boolean isSingleton(Element e) {
		String singleton = getElementAttributeValue(e, Constants.BEAN_PROPERTY_SINGLETON);
		if (singleton != null) {
			return Boolean.parseBoolean(singleton);
		}
		//默认是单例
		return true;
	}
	
	/**
	 * 得到Bean元素节点中，构造函数节点元素集合
	 * @param e bean元素对象
	 * @return 构造函数节点元素集合
	 */
	public List<Element> getConstructorElements(Element e) {
		return getSubElementsByName(e, Constants.BEAN_ELEMENT_CONSTRUCTOR_ARG);
	}
	
	public List<DataElement> getConstructorData(Element e) {
		List<Element> cons = getConstructorElements(e);
		List<DataElement> consData = new ArrayList<>();
		for (Element con : cons) {
			// opp!
			Element d = (Element)con.elements().get(0);
			consData.add(getDataElement(d));
		}
		return consData;
	}
	
	private DataElement getDataElement(Element e) {
		DataElement de = null;
		if (e.getName().equals(Constants.CONSTRUCTOR_ARG_TYPE_VALUE)) {
			String type = this.getElementAttributeValue(e, Constants.BEAN_VALUE_TYPE);
			String value = e.getText();
			try {
				Class<?> clazz = Class.forName(type);
				Constructor<?> constructor = clazz.getConstructor(String.class);
				de = new ValueElement(constructor.newInstance(value));
			} catch (Exception ex) {
				throw new XmlParseException(ex.getMessage());
			}
		} else {
			de = new ReferElement(getElementAttributeValue(e, 
					Constants.CONSTRUCTOR_ARG_REF_ARRT_BEAN));
		}
		return de;
	}

	/**
	 * 得到Bean元素节点中，属性节点元素集合
	 * @param be bean元素对象
	 * @return 属性节点元素集合
	 */
	public List<Element> getPropertyElements(Element be) {
		return getSubElementsByName(be, Constants.BEAN_ELEMENT_PROPERTY);
	}
	
	public List<PropertyElement> getPropertyData(Element be) {
		List<Element> properties = this.getPropertyElements(be);
		List<PropertyElement> propertyData = new ArrayList<>();
		for (Element property : properties) {
			Element d = (Element)property.elements().get(0);
			String name = this.getElementAttributeValue(property, Constants.BEAN_PROPERTY_NAME);
			DataElement de = getDataElement(d);
			PropertyElement pe = new PropertyElement(name, de);
			propertyData.add(pe);
		}
		return propertyData;
	}
	
	/**
	 * 得到bean元素节点自动装配属性
	 * @param be bean元素对象
	 * @return 自动装配对象
	 */
	public Autowire getAutowire(Element be) {
		String pvalue = getElementAttributeValue(be.getParent(), Constants.BEANS_PROPERTY_DEFAULT_AUTOWIRE);
		String value = this.getElementAttributeValue(be, Constants.BEAN_PROPERTY_AUTOWIRE);
		
		if (Constants.BEAN_PROPERTY_AUTOWIRE_BYNAME.equals(pvalue)) {
			
			if (Constants.BEAN_PROPERTY_AUTOWIRE_NO.equals(value)) {
				return new NoAutowire();
			} else {
				return new ByNameAutowire();
			}
			
		} else {
			
			if (Constants.BEAN_PROPERTY_AUTOWIRE_BYNAME.equals(value)) {
				return new ByNameAutowire();
			} else {
				return new NoAutowire();
			}
			
		}
	}
}
