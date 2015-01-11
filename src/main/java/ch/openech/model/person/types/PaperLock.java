package ch.openech.model.person.types;

import ch.openech.model.types.EchCode;

public enum PaperLock implements EchCode {

	keine_sperre, sperre;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal());
	}

}
