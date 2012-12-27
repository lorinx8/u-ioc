package me.bukp.uioc.domain;

public abstract class DataElement {
	
	protected Object value;
	
	protected DataElement(Object value) {
		this.value = value;
	}

	public abstract String getType();
	
	public Object getValue() {
		return value;
	}
}
