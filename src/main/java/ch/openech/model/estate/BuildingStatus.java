package ch.openech.model.estate;

import ch.openech.model.types.EchCode;

public enum BuildingStatus implements EchCode {

	_1001, _1002, _1003, _1004, _1005, _1007, _1008;

	@Override
	public String getValue() {
		return name().substring(1);
	}

}
