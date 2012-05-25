package ch.openech.client.org;

import ch.openech.dm.organisation.Organisation;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0097;
import ch.openech.xml.write.WriterEch0098;
import ch.openech.xml.write.WriterEch0148;
import ch.openech.xml.write.WriterElement;

public class KeyDeliveryOrganisationAction extends ExportAllOrganisationAction {
	
	private final WriterEch0097 ech97;
	
	public KeyDeliveryOrganisationAction(EchSchema echNamespaceContext) {
		super(echNamespaceContext);
		ech97 = new WriterEch0097(echNamespaceContext);
	}

	@Override
	protected WriterElement createBaseDelivery(WriterEch0148 writer, WriterElement delivery)
			throws Exception {
		return writer.keyExchange(delivery);
	}

	@Override
	protected void writeOrganisation(WriterEch0098 writer, WriterElement baseDelivery, Organisation organisation) throws Exception {
		ech97.organisationIdentification(baseDelivery, organisation);
	}
	
	@Override
	protected void writeNumberOfOrganisations(WriterEch0148 writer, WriterElement baseDelivery, int numberOfOrganisations) throws Exception {
		// do nothing
	}
}
