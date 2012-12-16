package ch.openech.dm.person.types;

import ch.openech.dm.types.EchCode;

public enum DataLock implements EchCode {

	ungesperrt, Adresssperre, Auskunftssperre;

	@Override
	public String getValue() {
		return String.valueOf(ordinal());
	}
}

