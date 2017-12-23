package ch.openech.model.estate;

import ch.openech.model.types.EchCode;

public enum DwellingUsage implements EchCode {

	_3010, _3020, _3030, _3031, _3032, _3033, _3034, _3035, _3036, _3037, _3038, _3070;

	@Override
	public String getValue() {
		return name().substring(1);
	}

}
