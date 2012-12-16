package ch.openech.dm.person.types;

import ch.openech.dm.types.EchCode;

public enum Separation implements EchCode {
	freiwillig,
	gerichtlich;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}
}
