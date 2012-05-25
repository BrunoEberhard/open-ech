package ch.openech.client.ewk.event;

import ch.openech.dm.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterElement;

public class KeyDeliveryPersonAction extends ExportAllPersonAction {
	
	public KeyDeliveryPersonAction(EchSchema echNamespaceContext) {
		super(echNamespaceContext);
	}

	@Override
	protected WriterElement createBaseDelivery(WriterEch0020 writer, WriterElement delivery, int maxId)
			throws Exception {
		return writer.keyDelivery(delivery, maxId);
	}

	@Override
	protected void writePerson(WriterEch0020 writer, WriterElement baseDelivery, Person person) throws Exception {
		writer.eventKeyDelivery(baseDelivery, person.personIdentification);
	}
}
