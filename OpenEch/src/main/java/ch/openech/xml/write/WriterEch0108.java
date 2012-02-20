package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.*;

import ch.openech.dm.organisation.Organisation;

public class WriterEch0108 extends DeliveryWriter {

	public final String URI;
	public final WriterEch0098 ech98;
	
	public WriterEch0108(EchNamespaceContext context) {
		super(context);
		
		URI = context.getNamespaceURI(108);
		ech98 = new WriterEch0098(context);
	}
	
	@Override
	protected int getSchemaNumber() {
		return 108;
	}
	
	@Override
	protected String getNamespaceURI() {
		return URI;
	}
	
	public String organisation(Organisation values) throws Exception {
        WriterElement root = delivery();
		organisation(root, values);
        return result();
	}
	
	public void organisation(WriterElement parent, Organisation values) throws Exception {
		organisation(parent, ORGANISATION, values);
	}
	
	public void organisation(WriterElement parent, String tagName, Organisation values) throws Exception {
		WriterElement element = parent.create(URI, tagName);

		ech98.organisation(element, values);
		uidregInformation(element, values);
		commercialRegisterInformation(element, values);
		vatRegisterInformation(element, values);
	}
	
	private void uidregInformation(WriterElement parent, Organisation values) throws Exception {
		WriterElement element = parent.create(URI, UIDREG_INFORMATION);
		element.values(values, UIDREG_STATUS_ENTERPRISE_DETAIL, //
				UIDREG_PUBLIC_STATUS, UIDREG_ORGANISATION_TYPE, //
				UIDREG_LIQUIDATION_REASON);
		// TODO source
		// WriterEch0097.uidStructure(element, "uidRegSource", values.uidregSourceOrganisationId)
    }

	private void commercialRegisterInformation(WriterElement parent, Organisation values) throws Exception {
		WriterElement element = parent.create(URI, COMMERCIAL_REGISTER_INFORMATION);
		element.values(values, COMMERCIAL_REGISTER_STATUS, //
				COMMERCIAL_REGISTER_ENTRY_STATUS, COMMERCIAL_REGISTER_NAME_TRANSLATION, //
				COMMERCIAL_REGISTER_NAME_TRANSLATION, COMMERCIAL_REGISTER_ENTRY_DATE,
				COMMERCIAL_REGISTER_LIQUIDATION_DATE);
    }
	
	private void vatRegisterInformation(WriterElement parent, Organisation values) throws Exception {
		WriterElement element = parent.create(URI, VAT_REGISTER_INFORMATION);
		element.values(values, VAT_STATUS, VAT_ENTRY_STATUS, VAT_ENTRY_DATE, VAT_LIQUIDATION_DATE);
    }
	
}
