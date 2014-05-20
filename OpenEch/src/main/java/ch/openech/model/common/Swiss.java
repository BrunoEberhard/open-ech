package  ch.openech.model.common;


public class Swiss {

	public static final Integer SWISS_COUNTRY_ID = 8100;
	public static final String SWISS_COUNTRY_ISO2 = "CH";
	public static final String SWISS_COUNTRY_NAME_SHORT = "Schweiz";

	public static CountryIdentification createCountryIdentification() {
		CountryIdentification countryIdentification = new CountryIdentification();
		countryIdentification.countryId = SWISS_COUNTRY_ID;
		countryIdentification.countryIdISO2 = SWISS_COUNTRY_ISO2;
		countryIdentification.countryNameShort = SWISS_COUNTRY_NAME_SHORT;
		return countryIdentification;
	}
	
}
