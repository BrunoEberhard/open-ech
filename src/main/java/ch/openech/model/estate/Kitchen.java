package ch.openech.model.estate;

import ch.openech.model.types.EchCode;

public enum Kitchen implements EchCode {

	_7300, _7301, _7302;

	@Override
	public String getValue() {
		return name().substring(1);
	}

}
