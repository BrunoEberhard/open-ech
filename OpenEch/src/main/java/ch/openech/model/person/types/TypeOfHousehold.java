package ch.openech.model.person.types;

import ch.openech.model.types.EchCode;

public enum TypeOfHousehold implements EchCode {

	privathaushalt, kollektivhaushalt, sammelhaushalt, nochNichtZugeteilt;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal()+1);
	}

}
