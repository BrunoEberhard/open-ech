package ch.openech.model.estate;

import ch.openech.model.types.EchCode;

public enum UsageLimitation implements EchCode {

	_3401, _3402, _3403, _3404;

	@Override
	public String getValue() {
		return name().substring(1);
	}

}
