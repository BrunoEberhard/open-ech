package  ch.openech.model.person.types;

import  ch.openech.model.types.EchCode;

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
