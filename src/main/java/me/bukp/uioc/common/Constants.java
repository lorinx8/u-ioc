package me.bukp.uioc.common;

/**
 * 常量定义
 * 2012-12-18
 */
public class Constants {
	
	//配置文件中出现的常量字符串
	public static final String BEANS_PROPERTY_DEFAULT_LAZY_INIT = "default-lazy-init";
	public static final String BEAN_PROPERTY_ID = "id";
	public static final String BEAN_PROPERTY_LAZY_INIT = "lazy-init";
	public static final String BEAN_PROPERTY_SINGLETON = "singleton";
	public static final String BEAN_ELEMENT_CONSTRUCTOR_ARG = "constructor-arg";
	public static final String BEAN_ELEMENT_PROPERTY = "property";
	
	//属性数据类型，值类型或者引用类型
	public static final String BEAN_DATA_TYPE_VALUE = "value";
	public static final String BEAN_DATA_TYPE_REFER = "refer";
	
	//错误信息
	public static final String EXCEPTION_MESSAGE_NO_CONSTRUCTORS = "no constructors found";
	public static final String EXCEPTION_MESSAGE_NO_METHODS = "no methods found";
}
