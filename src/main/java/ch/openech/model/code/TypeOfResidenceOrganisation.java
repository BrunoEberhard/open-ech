package  ch.openech.model.code;

import  ch.openech.model.types.EchCode;

public enum TypeOfResidenceOrganisation implements EchCode {

	hasMainResidence,
	hasSecondaryResidence,
	hasOtherResidence;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}
	
}
