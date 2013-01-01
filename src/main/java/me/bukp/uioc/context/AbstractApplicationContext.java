package me.bukp.uioc.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import me.bukp.uioc.common.Constants;
import me.bukp.uioc.domain.Autowire;
import me.bukp.uioc.domain.ByNameAutowire;
import me.bukp.uioc.domain.DataElement;
import me.bukp.uioc.domain.PropertyElement;
import me.bukp.uioc.exception.BeanCreateException;
import me.bukp.uioc.xml.BeanElementHandler;

public abstract class AbstractApplicationContext implements ApplicationContext {

	protected BeanElementHandler elementHandler = new BeanElementHandler();
	
	// 用来缓存beans
	protected Map<String, Object> beanPool = new HashMap<>();

	/**
	 * 初始化bean元素对象
	 * @param paths
	 */
	protected abstract void InitBeanElements(String[] paths);
	
	/* (non-Javadoc)
	 * @see me.bukp.uioc.context.ApplicationContext#containsBean(java.lang.String)
	 */
	@Override
	public boolean containsBean(String id) {
		Element e = elementHandler.getBeanElement(id);
		return (e == null) ? false : true;
	}

	/* (non-Javadoc)
	 * @see me.bukp.uioc.context.ApplicationContext#isSingleton(java.lang.String)
	 */
	@Override
	public boolean isSingleton(String id) {
		Element be = elementHandler.getBeanElement(id);
		return elementHandler.isSingleton(be);
	}

	protected Autowire getAutowire(String id) {
		Element be = elementHandler.getBeanElement(id);
		return elementHandler.getAutowire(be);
	}
	
	
	/* (non-Javadoc)
	 * @see me.bukp.uioc.context.ApplicationContext#getBean(java.lang.String, boolean)
	 */
	@Override
	public Object getBean(String id, boolean create) {
		// 如果不是单例的，则直接创建返回
		if (!isSingleton(id)) {
			return createBean(id);
		}
		Object obj = beanPool.get(id);
		if (obj != null) {
			return obj;
		}
		if (create) {
			obj = createBean(id);
			
			//自动装配，设值注入
			Autowire autowire = this.getAutowire(id);
			if (autowire instanceof ByNameAutowire) {
				//自动装配
				this.autowireByName(obj);
			} else {
				//根据属性设值注入
				this.propertiesInject(obj, id);
			}
			beanPool.put(id, obj);
			return obj;
		}
		return null;
	}

	/**
	 * 根据bean id创建bean
	 * @param id bean id
	 * @return 创建出的bean对象
	 */
	protected Object createBean(String id) {
		Element be = elementHandler.getBeanElement(id);
		if (be == null) {
			throw new BeanCreateException(Constants.EXCEPTION_MESSAGE_NO_ELEMENT);
		}
		return Instance(be);
	}
	
	/**
	 * 根据bean节点元素实例化bean
	 * @param be xml文件中的bean元素对象
	 * @return bean对象
	 */
	protected Object Instance(Element be) {
		String className = elementHandler.getBeanClassName(be);
		List<Object> args = getConstructorArgs(be);
		if (args.size() == 0) {
			return BeanCreator.createBean(className);
		} else {
			return BeanCreator.createBean(className, args);
		}
	}
	
	/**
	 * 根据xml的bean节点元素对象得到构造函数参数对象
	 * @param be bean节点元素对象
	 * @return 构造函数参数对象集合
	 */
	protected List<Object> getConstructorArgs(Element be){
		List<Object> args = new ArrayList<>();
		List<DataElement> des = elementHandler.getConstructorData(be);
		if (des != null && des.size() > 0) {
			for (DataElement de : des) {
				if (de.getType().equals(Constants.BEAN_DATA_TYPE_VALUE)) {
					args.add(de.getValue());
				}
				else {
					String refid = (String)de.getValue();
					args.add(getBean(refid, true));
				}
			}
		}
		return args;
	}
	
	/**
	 * 通过属性名进行自动注入
	 * @param 待注入的对象
	 */
	protected void autowireByName(Object obj) {
		Map<String, Method> setters = PropertyHandler.getSetterMethods(obj);
		for (String property : setters.keySet()) {
			Element e = elementHandler.getBeanElement(property);
			if (e == null) {
				continue;
			}
			Object bean = this.getBean(property, true);
			Method method = setters.get(property);
			PropertyHandler.executeSetterMethod(obj, bean, method);
		}
	}
	
	/**
	 * 根据属性进行设值注入
	 * @param obj 待注入的对象
	 * @param be xml文件中的bean元素对象
	 */
	protected void propertiesInject(Object obj, String id) {
		Element be = elementHandler.getBeanElement(id);
		List<PropertyElement> properties = elementHandler.getPropertyData(be);
		Map<String, Object> propertiesMap = new HashMap<>();
		
		for (PropertyElement property : properties) {
			String name = property.getName();
			Object value = property.getDataElement().getValue();
			propertiesMap.put(name, value);
		}
		PropertyHandler.setProperties(obj, propertiesMap);
	}
}
