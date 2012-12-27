package me.bukp.uioc.domain;

import me.bukp.uioc.common.Constants;

public class ValueElement extends DataElement {

	public ValueElement(Object value) {
		super(value);
	}
	
	@Override
	public String getType() {
		return Constants.BEAN_DATA_TYPE_VALUE;
	}
}