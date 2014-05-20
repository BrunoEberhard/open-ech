package  ch.openech.model.types;


public enum MrMrs implements EchCode {
	Frau,
	Herr,
	Fraeulein;

	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}
}
