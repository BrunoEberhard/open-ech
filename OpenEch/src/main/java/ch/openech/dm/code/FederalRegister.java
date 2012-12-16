package ch.openech.dm.code;

import ch.openech.dm.types.EchCode;

// Bundesregister System
public enum FederalRegister implements EchCode {

	Infostar, Ordipro, ZEMIS;
			
	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}

}
