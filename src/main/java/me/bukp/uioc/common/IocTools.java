package me.bukp.uioc.common;

/**
 * 一些通用方法
 * 2012-12-26
 */
public class IocTools {
	/**
	 * 通过强制转换判断参数类型是否一致
	 * @param srcClasses 源参数类型
	 * @param destClasses 待比较的参数类型
	 * @return 参数类型是否一致
	 */
	public static boolean ifSameArgsClass(Class<?>[] srcClasses, Class<?>[] destClasses) {
		if (srcClasses.length != destClasses.length) {
			return false;
		}
		boolean result = true;
		for (int i = 0; i < destClasses.length; i++) {
			try {
				srcClasses[i].asSubclass(destClasses[i]);
			} catch (ClassCastException e) {
				result = false;
				break;
			}
		}
		return result;
	}
}
