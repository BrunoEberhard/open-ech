package  ch.openech.model.types;


public enum TypeOfResidence implements EchCode {
	hasMainResidence,
	hasSecondaryResidence,
	hasOtherResidence;
	
	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}

}
