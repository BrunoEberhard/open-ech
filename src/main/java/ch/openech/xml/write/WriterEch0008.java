package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.COUNTRY_ID;
import static ch.openech.model.XmlConstants.COUNTRY_ID_I_S_O2;
import static ch.openech.model.XmlConstants.COUNTRY_NAME_SHORT;

import ch.openech.model.common.CountryIdentification;

public class WriterEch0008 {

	public final String URI;
	
	public WriterEch0008(EchSchema context) {
		URI = context.getNamespaceURI(8);
	}
	
	public void country(WriterElement parent, String tagName, CountryIdentification country) throws Exception {
		country(URI, parent, tagName, country);
	}
	
	public void country(String URI, WriterElement parent, String tagName, CountryIdentification country) throws Exception {
		if (country == null || country.isEmpty()) return;
		WriterElement writer = parent.create(URI, tagName);
		writer.text(COUNTRY_ID, country.id);
		writer.text(COUNTRY_ID_I_S_O2, country.countryIdISO2);
		writer.text(COUNTRY_NAME_SHORT, country.countryNameShort);
	}
	
}
