package ch.openech.frontend.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Person;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.xml.write.WriterEch0020;

public class NaturalizeForeignerEvent extends NaturalizeSwissEvent {

	public NaturalizeForeignerEvent(PersonPage personPage) {
		super(personPage);
	}

	@Override
	protected List<String> getXml(Person person, PlaceOfOrigin placeOfOrigin, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.naturalizeForeigner(person.personIdentification(), placeOfOrigin));
	}
	
}
