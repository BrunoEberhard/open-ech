package  ch.openech.model.code;

import  ch.openech.model.types.EchCode;

public enum NationalityStatus implements EchCode {

	unknown, without, with;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal());
	}

}
