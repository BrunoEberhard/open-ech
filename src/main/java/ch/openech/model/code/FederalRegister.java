package  ch.openech.model.code;

import  ch.openech.model.types.EchCode;

// Bundesregister System
public enum FederalRegister implements EchCode {

	Infostar, Ordipro, ZEMIS;
			
	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}

}
