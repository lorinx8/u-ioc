package me.bukp.uioc.exception;

/**
 * xml解析异常类，用于包装xml解析过程中的异常
 */
public class XmlParseException extends RuntimeException {
	
	private static final long serialVersionUID = -829593340455850672L;
	
	public XmlParseException() {}
	
	public XmlParseException(String message) {
		super(message);
	}
}
