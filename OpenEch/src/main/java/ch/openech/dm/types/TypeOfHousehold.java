package ch.openech.dm.types;


public enum TypeOfHousehold implements EchCode {
	noch_nicht_zugeteilt,
	Privathaushalt,
	Kollektivhaushalt,
	Sammelhaushalt;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal());
	}
}
