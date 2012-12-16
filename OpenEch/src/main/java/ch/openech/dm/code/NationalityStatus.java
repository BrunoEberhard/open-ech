package ch.openech.dm.code;

import ch.openech.dm.types.EchCode;

public enum NationalityStatus implements EchCode {

	unknown, without, with;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal());
	}

}
