package ch.openech.model.estate;

import ch.openech.model.types.EchCode;

public enum DwellingStatus implements EchCode {

	_3001, _3002, _3003, _3004, _3005, _3007, _3008;

	@Override
	public String getValue() {
		return name().substring(1);
	}

}
