package me.bukp.uioc.domain;

import me.bukp.uioc.common.Constants;

public class ReferElement extends DataElement {

	public ReferElement(Object value) {
		super(value);
	}
	
	@Override
	public String getType() {
		return Constants.BEAN_DATA_TYPE_REFER;
	}
}
