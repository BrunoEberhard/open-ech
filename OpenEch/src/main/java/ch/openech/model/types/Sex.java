package  ch.openech.model.types;


public enum Sex implements EchCode {
	maennlich,
	weiblich;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}

}