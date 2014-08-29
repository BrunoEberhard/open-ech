package ch.openech.model.person.types;

import ch.openech.model.types.EchCode;

public enum KindOfEmployment implements EchCode {

	erwerbslos, selbstaendiger, unselbstaendiger, ahvIv, nichterwerbsperson;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal());
	}

}
