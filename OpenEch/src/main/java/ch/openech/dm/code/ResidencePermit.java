package ch.openech.dm.code;

import ch.openech.dm.types.EchCode;

public enum ResidencePermit implements EchCode {

	Test("11"),
	Saisonarbeiter("01"), Saisonarbeiter_nicht_eu_efta_abkommen("0102"),
	Aufenthalter("02"), Aufenthalter_nach_eu_efta_abkommen("0201");

	private final String value;
	
	// do not remove, used by reflection
	private ResidencePermit() {
		this.value = null;
	}
	
	private ResidencePermit(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}
	
}
