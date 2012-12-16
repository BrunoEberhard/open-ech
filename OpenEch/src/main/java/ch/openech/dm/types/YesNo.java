package ch.openech.dm.types;


public enum YesNo implements EchCode {
	No,
	Yes;

	@Override
	public String getValue() {
		return String.valueOf(ordinal());
	}
	
}
