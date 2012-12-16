package ch.openech.dm.person.types;

import ch.openech.dm.types.EchCode;

public enum Religion implements EchCode {
	evangelisch(111),
	roemisch_katolisch(121),
	christ_katolisch(122),
	juedisch(211),
	konfessionslos(711),
	nicht_anerkannt(811),
	unbekannt(0);
	
	private String value;

	private Religion() {
		this(0);
	}
	
	private Religion(int value) {
		if (value == 0) {
			this.value = "000";
		} else {
			this.value = String.valueOf(value);
		}
	}

	@Override
	public String getValue() {
		return value;
	}
}
