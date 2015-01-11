package  ch.openech.model.types;

public enum ContactCategory implements EchCode {

	privat,
	geschaeftlich;

	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}
			
}
