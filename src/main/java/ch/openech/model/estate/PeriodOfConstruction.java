package ch.openech.model.estate;

import ch.openech.model.types.EchCode;

public enum PeriodOfConstruction implements EchCode {

	_8011, _8012, _8013, _8014, _8015, _8016, _8017, _8018, _8019, _8020, _8021, _8022, _8023;

	@Override
	public String getValue() {
		return name().substring(1);
	}

}
