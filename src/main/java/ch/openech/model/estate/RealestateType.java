package ch.openech.model.estate;

import ch.openech.model.types.EchCode;

public enum RealestateType implements EchCode {

	Liegenschaft, Stockwerkseinheit, GewoehnlichesMiteigentum, Konzession, GewoehnlichesSDR, Bergwerk, Quellenrecht, weitere;

	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}

}
