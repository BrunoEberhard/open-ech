package ch.openech.frontend.ewk.event;

import java.util.Collections;
import java.util.List;

import  ch.openech.model.person.Person;
import  ch.openech.model.person.PlaceOfOrigin;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class NaturalizeForeignerEvent extends NaturalizeSwissEvent {

	public NaturalizeForeignerEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected List<String> getXml(Person person, PlaceOfOrigin placeOfOrigin, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.naturalizeForeigner(person.personIdentification(), placeOfOrigin));
	}
	
}
