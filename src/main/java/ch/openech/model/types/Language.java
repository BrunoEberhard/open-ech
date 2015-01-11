package  ch.openech.model.types;

public enum Language implements EchCode {
	de, fr, it, rm, en;

	@Override
	public String getValue() {
		return name();
	}
}
