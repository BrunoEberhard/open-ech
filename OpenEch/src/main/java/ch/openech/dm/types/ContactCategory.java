package ch.openech.dm.types;

public enum ContactCategory implements EchCode {

	privat,
	geschaeftlich;

	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}
	
	
//	addressCategory.object=Kategorie
//			addressCategory.default=1
//			addressCategory.key.0=1
//			addressCategory.text.0=Private Postadresse
//			addressCategory.key.1=2
//			addressCategory.text.1=Geschäftliche Postadresse

			
}
