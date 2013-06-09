package ch.openech.dm.person.types;

import ch.openech.dm.types.EchCode;

public enum PartnerShipAbolition implements EchCode {

	unbekannt, gerichtlichAufgeloest, ungueltig, verschollen, tod;

	@Override
	public String getValue() {
		if (this == unbekannt) {
			return "9";
		} else {
			return String.valueOf(ordinal());
		}
	}
			
}
