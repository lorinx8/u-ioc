package me.bukp.uioc.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import me.bukp.uioc.common.Constants;
import me.bukp.uioc.domain.DataElement;
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
		Element e = elementHandler.getBeanElement(id);
		return elementHandler.isSingleton(e);
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
			beanPool.put(id, obj);
			return obj;
		}
		
		return null;
	}
	
	protected Object createBean(String id) {
		Element be = elementHandler.getBeanElement(id);
		if (be == null) {
			throw new BeanCreateException(Constants.EXCEPTION_MESSAGE_NO_ELEMENT);
		}
		return Instance(be);
	}
	
	protected Object Instance(Element be) {
		String className = elementHandler.getBeanClassName(be);
		List<Object> args = getConstructorArgs(be);
		if (args.size() == 0) {
			return BeanCreator.createBean(className);
		} else {
			return BeanCreator.createBean(className, args);
		}
	}
	
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
}
