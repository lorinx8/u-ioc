package me.bukp.uioc.exception;

/**
 * 属性处理异常类，用于包装处理属性过程中的异常
 */
public class PropertyHandleException extends RuntimeException {

	private static final long serialVersionUID = 2842104824868662334L;
	
	public PropertyHandleException() {}
	
	public PropertyHandleException(String message) {
		super(message);
	}
}