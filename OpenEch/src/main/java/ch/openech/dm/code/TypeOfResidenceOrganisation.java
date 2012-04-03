package ch.openech.dm.code;

import ch.openech.mj.db.model.CodeValue;

public enum TypeOfResidenceOrganisation implements CodeValue {

	Hauptsitz(1), Nebensitz(2), Anderersitz(3);
	
	private final String value;
	
	private TypeOfResidenceOrganisation(int value) {
		this.value = Integer.toString(value);
	}
	
	@Override
	public String getKey() {
		return value;
	}
	
	public static String getDefault() {
		return Hauptsitz.getKey();
	}
	
}
