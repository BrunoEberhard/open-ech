package  ch.openech.model.person.types;

import  ch.openech.model.types.EchCode;

public enum Separation implements EchCode {
	freiwillig,
	gerichtlich;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}
}
