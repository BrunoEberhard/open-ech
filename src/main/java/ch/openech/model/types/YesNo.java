package  ch.openech.model.types;


public enum YesNo implements EchCode {
	No,
	Yes;

	@Override
	public String getValue() {
		return String.valueOf(ordinal());
	}
	
}
