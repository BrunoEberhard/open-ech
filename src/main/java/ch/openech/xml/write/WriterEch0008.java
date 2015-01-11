package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;
import ch.openech.model.common.CountryIdentification;

public class WriterEch0008 {

	public final String URI;
	
	public WriterEch0008(EchSchema context) {
		URI = context.getNamespaceURI(8);
	}
	
	public void country(WriterElement parent, String tagName, CountryIdentification countryIdentification) throws Exception {
		if (countryIdentification == null || countryIdentification.isEmpty()) return;
		WriterElement writer = parent.create(URI, tagName);
		writer.text(COUNTRY_ID, countryIdentification.id);
		writer.text(COUNTRY_ID_I_S_O2, countryIdentification.countryIdISO2);
		writer.text(COUNTRY_NAME_SHORT, countryIdentification.countryNameShort);
	}
	
}
