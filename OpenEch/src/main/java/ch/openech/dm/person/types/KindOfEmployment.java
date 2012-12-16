package ch.openech.dm.person.types;

import ch.openech.dm.types.EchCode;

public enum KindOfEmployment implements EchCode {

	// Die nichterwerbsperson / 4 gibts nur im Schema 21, nicht im Schema 20 !!
	erwerbslos, selbstaendig, unselbstaendig, ahv_iv, nichterwerbsperson;

	@Override
	public String getValue() {
		return String.valueOf(ordinal());
	}
}

