package ch.openech.model.estate;

import ch.openech.model.types.EchCode;

public enum BuildingCategory implements EchCode {

	_1010, _1020, _1030, _1040, _1060, _1080;
	
	@Override
	public String getValue() {
		return name().substring(1);
	}

}
