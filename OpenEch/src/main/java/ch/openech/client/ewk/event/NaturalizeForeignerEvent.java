package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class NaturalizeForeignerEvent extends NaturalizeSwissEvent {

	public NaturalizeForeignerEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected List<String> getXml(Person person, PlaceOfOrigin placeOfOrigin, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.naturalizeForeigner(person.personIdentification, placeOfOrigin));
	}
	
}
