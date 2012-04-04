package ch.openech.dm.code;

import ch.openech.mj.db.model.CodeValue;

public enum ApplicationMode implements CodeValue {

	Normal(0), Entwicklermodus(1);
	
	private final String value;
	
	private ApplicationMode(int value) {
		this.value = Integer.toString(value);
	}

	@Override
	public String getKey() {
		return value;
	}

}
