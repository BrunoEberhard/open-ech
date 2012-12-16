package ch.openech.dm.person.types;

import ch.openech.dm.types.EchCode;

public enum PaperLock implements EchCode {

	ungesperrt, gesperrt;

	@Override
	public String getValue() {
		return String.valueOf(ordinal());
	}
}

