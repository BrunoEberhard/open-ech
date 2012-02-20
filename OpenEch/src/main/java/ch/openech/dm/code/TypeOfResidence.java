package ch.openech.dm.code;

import ch.openech.mj.db.model.CodeValue;

public enum TypeOfResidence implements CodeValue {

	Hauptwohnsitz(1), Nebenwohnsitz(2), Andererwohnsitz(3);
	
	private final String value;
	
	private TypeOfResidence(int value) {
		this.value = Integer.toString(value);
	}
	
	@Override
	public String getKey() {
		return value;
	}
	
	public static String getDefault() {
		return Hauptwohnsitz.getKey();
	}
	
}
