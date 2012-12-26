package me.bukp.uioc.context;

import java.lang.reflect.Constructor;
import java.util.List;

import me.bukp.uioc.common.Constant;
import me.bukp.uioc.common.IocTools;
import me.bukp.uioc.exception.BeanCreateException;

/**
 * 用于创建bean的静态工具类
 * 2012-12-18
 */
public class BeanCreator {
	
	/**
	 * 根据类名，调用类的无参构造函数创建bean
	 * @param className 类名
	 * @return 创建的bean对象
	 * @exception BeanCreateException
	 */
	public static Object createBean(String className) {
		try {
			Class<?> clazz = Class.forName(className);
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeanCreateException(e.getMessage());
		}
	}
	
	/**
	 * 根据类名，调用类的含参构造函数创建bean
	 * @param className 类名
	 * @param args 参数
	 * @return 创建的bean对象
	 * @exception BeanCreateException
	 */
	public static Object createBean(String className, List<Object> args) {
		Class<?>[] argsClass = getArgsClass(args);
		try {
			Class<?> clazz = Class.forName(className);
			Constructor<?> constructor = findConstructor(clazz, argsClass);
			return constructor.newInstance(args);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeanCreateException(e.getMessage());
		}
	}
	
	/**
	 * 根据入参数类型查找构造函数
	 * @param clazz 类
	 * @param argsClass 参数类型
	 * @return 符合入参类型的构造函数
	 * @exception BeanCreateException
	 */
	private static Constructor<?> findConstructor(Class<?> clazz, Class<?>[] argsClass) {
		Constructor<?> constructor = null;
		try {
			constructor = clazz.getConstructor(argsClass);
		} catch (Exception e) {
			constructor = findConstructorExtend(clazz, argsClass);
		}
		if (constructor != null) {
			return constructor;
		}
		throw new BeanCreateException(Constant.EXCEPTION_MESSAGE_NO_CONSTRUCTORS);
	}
	
	/**
	 * 如果类的构造器中的参数是某一接口，而传入的参数是接口的某个实现
	 * 直接使用getConstructor会抛出NoSuchMethodException异常
	 * 所以，取出Class的所有构造函数，并对传入的参数进行强制转换后与构造函数的参数进行比较
	 * @param clazz 类
	 * @param argsClass 参数类型 
	 * @return 符合入参类型的构造函数，若没有能匹配入参类型的构造函数，则返回null
	 */
	private static Constructor<?> findConstructorExtend(Class<?> clazz, Class<?>[] argsClass) {
		Constructor<?>[] constructors = clazz.getConstructors();
		for (Constructor<?> constructor : constructors) {
			Class<?>[] consArgsClass = constructor.getParameterTypes();
			if (IocTools.ifSameArgsClass(argsClass, consArgsClass)) {
				return constructor;
			}
		}
		return null;
	}
	
	/**
	 * 将对象列表转换为Class数组
	 * @param args 对象列表
	 * @return Class数组
	 */
	private static Class<?>[] getArgsClass(List<Object> args) {
		Class<?>[] clazzs = new Class[args.size()];
		for (int i = 0; i < args.size(); i++) {
			clazzs[i] = args.get(i).getClass();
		}
		return clazzs;
	}

}
