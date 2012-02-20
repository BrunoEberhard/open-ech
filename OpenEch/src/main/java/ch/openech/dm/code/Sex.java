package ch.openech.dm.code;

import ch.openech.mj.db.model.CodeValue;

public enum Sex implements CodeValue {

	Maennlich(1), Weiblich(2);
	
	private final String value;
	
	private Sex(int value) {
		this.value = Integer.toString(value);
	}
	
	@Override
	public String getKey() {
		return value;
	}
	
	public static String getDefault() {
		return Maennlich.getKey();
	}
	
}
