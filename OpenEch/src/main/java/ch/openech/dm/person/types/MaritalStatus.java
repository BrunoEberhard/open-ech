package ch.openech.dm.person.types;

import ch.openech.dm.types.EchCode;

public enum MaritalStatus implements EchCode {
	ledig, // Code 1
	verheiratet,
	verwitwet,
	geschieden,
	ungueltig,
	partnerschaft,
	aufgeloeste_partnerschaft;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}

}
