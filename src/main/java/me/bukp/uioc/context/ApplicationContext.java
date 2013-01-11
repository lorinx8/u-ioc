package me.bukp.uioc.context;

public interface ApplicationContext {
	
	
	/**
	 * 根据id从容器中得到bean，如果找不到，则返回null
	 * @param id id 配置文件中bean元素的id
	 * @return bean实例
	 */
	Object getBean(String id);
	
	/**
	 * 根据id从容器中得到bean，如果找不到，根据create值决定是否创建
	 * @param id 配置文件中bean元素的id
	 * @param create 如果容器中找不到bean，是否进行创建
	 * @return bean实例
	 */
	Object getBean(String id, boolean create);
	
	/**
	 * 根据id判断容器中是否包含bean
	 * @param id 配置文件中bean元素的id
	 * @return 容器中包含bean则返回true，否则返回false
	 */
	boolean containsBean(String id);
	
	/**
	 * 根据id判断bean是否为单态
	 * @param id 配置文件中bean元素的id
	 * @return bean为单态则返回true，否则返回false
	 */
	boolean isSingleton(String id);
}
