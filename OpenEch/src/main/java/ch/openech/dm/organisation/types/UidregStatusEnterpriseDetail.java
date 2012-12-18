package ch.openech.dm.organisation.types;

import ch.openech.dm.types.EchCode;

public enum UidregStatusEnterpriseDetail implements EchCode {

	provisorisch, in_Reaktivierung, definitiv, in_Mutation, geloescht, definitiv_geloecht, annulliert;
			
	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}

}
