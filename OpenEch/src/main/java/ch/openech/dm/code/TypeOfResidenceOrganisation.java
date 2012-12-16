package ch.openech.dm.code;

import ch.openech.dm.types.EchCode;

public enum TypeOfResidenceOrganisation implements EchCode {

	Hauptsitz, Nebensitz, Anderersitz;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}
	
}
