package me.bukp.uioc.exception;

/**
 * Bean创建异常类，用于包装创建Bean过程中的异常
 */
public class BeanCreateException extends RuntimeException {
	
	private static final long serialVersionUID = -8622525038901590151L;

	public BeanCreateException() {}
	
	public BeanCreateException(String message) {
		super(message);
	}
}
