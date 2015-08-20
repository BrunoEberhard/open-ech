package ch.openech.xml.read;

import static ch.openech.model.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.model.properties.FlatProperties;
import org.minimalj.util.StringUtils;

import ch.openech.model.common.NamedId;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;
import ch.openech.model.person.PersonIdentificationLight;

public class StaxEch0044 {

	public static PersonIdentification personIdentification(XMLEventReader xml) throws XMLStreamException {
		PersonIdentification personIdentification = new PersonIdentification();
		personIdentification(xml, personIdentification);
		return personIdentification;
	}
		
	public static void personIdentification(XMLEventReader xml, PersonIdentification personIdentification) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				
				if (startName.equals(LOCAL_PERSON_ID)) namedId(xml, personIdentification.technicalIds.localId);
				else if (StringUtils.equals(startName, OTHER_PERSON_ID, OTHER_PERSON_ID)) personIdentification.technicalIds.otherId.add(namedId(xml));
				else if (StringUtils.equals(startName, EU_PERSON_ID, "EuPersonId")) personIdentification.technicalIds.euId.add(namedId(xml));
				
				else if (StringUtils.equals(startName, OFFICIAL_NAME, FIRST_NAME)) {
					FlatProperties.set(personIdentification, startName, token(xml));
				}
				else if (StringUtils.equals(startName, VN)) {
					personIdentification.vn.value = token(xml);
				}
				else if (StringUtils.equals(startName, SEX)) {
					StaxEch.enuum(xml,  personIdentification, PersonIdentification.$.sex);
				}
				else if (startName.equals(DATE_OF_BIRTH)) {
					personIdentification.dateOfBirth.value = datePartiallyKnown(xml);
				}
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}

	public static void personIdentification(XMLEventReader xml, Person person) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				
				if (startName.equals(LOCAL_PERSON_ID)) namedId(xml, person.technicalIds.localId);
				else if (StringUtils.equals(startName, OTHER_PERSON_ID, OTHER_PERSON_ID)) person.technicalIds.otherId.add(namedId(xml));
				else if (StringUtils.equals(startName, EU_PERSON_ID, "EuPersonId")) person.technicalIds.euId.add(namedId(xml));
				
				else if (StringUtils.equals(startName, OFFICIAL_NAME, FIRST_NAME)) {
					FlatProperties.set(person, startName, token(xml));
				}
				else if (StringUtils.equals(startName, VN)) {
					person.vn.value = token(xml);
				}
				else if (StringUtils.equals(startName, SEX)) {
					StaxEch.enuum(xml,  person, Person.$.sex);
				}
				else if (startName.equals(DATE_OF_BIRTH)) {
					person.dateOfBirth.value = datePartiallyKnown(xml);
				}
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}

	public static void personIdentificationPartner(XMLEventReader xml, PersonIdentificationLight personIdentification) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();

				if (startName.equals(LOCAL_PERSON_ID)) {
					// Wird ignoriert. Wenn der Client eine solche id übermitteln will
					// sollte er die normale PersonIdentification verwenden
					skip(xml);
				} else if (StringUtils.equals(startName, OTHER_PERSON_ID, OTHER_PERSON_ID)) {
					personIdentification.otherId.add(namedId(xml));
				} else if (StringUtils.equals(startName, OFFICIAL_NAME, FIRST_NAME)) {
					FlatProperties.set(personIdentification, startName, token(xml));
				} else if (StringUtils.equals(startName, VN)) {
					personIdentification.vn.value = token(xml);
				} else if (StringUtils.equals(startName, SEX)) {
					StaxEch.enuum(xml, personIdentification, PersonIdentification.$.sex);
				} else if (startName.equals(DATE_OF_BIRTH)) {
					personIdentification.dateOfBirth.value = datePartiallyKnown(xml);
				} else
					skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	public static NamedId namedId(XMLEventReader xml) throws XMLStreamException {
		NamedId namedId = new NamedId();
		namedId(xml, namedId);
		return namedId;
	}
	
	public static void namedId(XMLEventReader xml, NamedId namedId) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				if (startElement.getName().getLocalPart().equals(PERSON_ID_CATEGORY)) namedId.personIdCategory = token(xml);
				else if (startElement.getName().getLocalPart().equals(PERSON_ID)) namedId.personId = token(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}

	// also used by 98
	public static String datePartiallyKnown(XMLEventReader xml) throws XMLStreamException {
		String date = null;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(YEAR_MONTH_DAY) || startName.equals(YEAR_MONTH) || startName.equals(YEAR)) {
					date = StaxEch.partial(xml);
				} else {
					throw new IllegalArgumentException();
				}
			} else if (event.isEndElement()) {
				// if (date == null) throw new IllegalArgumentException();
				return date;
			} // else skip
		}
	}
	
}
