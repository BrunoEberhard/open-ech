package ch.openech.model.person.types;

import ch.openech.model.types.EchCode;

public enum DataLock implements EchCode {

	keine_sperre, adresssperre, auskunftssperre;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal());
	}

}
