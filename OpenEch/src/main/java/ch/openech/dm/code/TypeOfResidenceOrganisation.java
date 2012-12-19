package ch.openech.dm.code;

import ch.openech.dm.types.EchCode;

public enum TypeOfResidenceOrganisation implements EchCode {

	hasMainResidence,
	hasSecondaryResidence,
	hasOtherResidence;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}
	
}
