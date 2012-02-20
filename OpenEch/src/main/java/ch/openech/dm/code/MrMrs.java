package ch.openech.dm.code;

import ch.openech.mj.db.model.CodeValue;

public enum MrMrs implements CodeValue {

	Frau(0), Herr(1), Fraeulein(2);
	
	public final String key;
	
	private MrMrs(int value) {
		this.key = Integer.toString(value);
	}
	
	@Override
	public String getKey() {
		return key;
	}
}
