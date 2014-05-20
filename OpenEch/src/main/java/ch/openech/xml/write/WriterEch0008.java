package ch.openech.xml.write;

import  ch.openech.model.common.CountryIdentification;

public class WriterEch0008 {

	public final String URI;
	
	public WriterEch0008(EchSchema context) {
		URI = context.getNamespaceURI(8);
	}
	
	public void country(WriterElement parent, String tagName, CountryIdentification countryIdentification) throws Exception {
		if (countryIdentification == null || countryIdentification.isEmpty()) return;
		WriterElement writer = parent.create(URI, tagName);
    	writer.values(countryIdentification);
	}
	
}
