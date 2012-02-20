package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.*;

import ch.openech.dm.organisation.Organisation;

public class WriterEch0098 {

	public final String URI;
	public final WriterEch0097 ech97;
	
	public WriterEch0098(EchNamespaceContext context) {
		URI = context.getNamespaceURI(98);
		ech97 = new WriterEch0097(context);
	}

	public void organisation(WriterElement parent, Organisation values) throws Exception {
		organisation(parent, ORGANISATION, values);
	}
	
	public void organisation(WriterElement parent, String tagName, Organisation values) throws Exception {
		WriterElement element = parent.create(URI, tagName);

		ech97.organisationIdentification(element, values);
		element.values(values, UID_BRANCHE_TEXT, NOGA_CODE);
		foundation(element, values);
		liquidation(element, values);
		// TODO Contact
		element.values(values, LANGUAGE_OF_CORRESPONDANCE);
	}
	
	private void foundation(WriterElement parent, Organisation values) throws Exception {
		WriterElement element = parent.create(URI, FOUNDATION);
		datePartiallyKnownType(element, FOUNDATION_DATE, values);
		element.values(values, FOUNDATION_REASON);
    }

	private void liquidation(WriterElement parent, Organisation values) throws Exception {
		WriterElement element = parent.create(URI, LIQUIDATION);
		datePartiallyKnownType(element, LIQUIDATION_DATE, values);
		element.values(values, LIQUIDATION_REASON);
    }

	public void datePartiallyKnownType(WriterElement parent, String tagName, Object object) throws Exception {
		WriterEch0044.datePartiallyKnownType(parent, URI, tagName, object);
	}
	
}
