package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.person.Occupation;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Relation;
import ch.openech.mj.util.StringUtils;

public class StaxEch0021 {

	public static Occupation occupation(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		Occupation occupation = new Occupation();
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(JOB_TITLE)) occupation.jobTitle = token(xml);
				else if (startName.equals(KIND_OF_EMPLOYMENT)) occupation.kindOfEmployment = token(xml);
				else if (startName.equals(EMPLOYER)) occupation.employer = token(xml);
				else if (startName.equals(PLACE_OF_WORK)) occupation.placeOfWork = StaxEch0010.address(xml);
				else if (startName.equals(PLACE_OF_EMPLOYER)) occupation.placeOfEmployer = StaxEch0010.address(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				return occupation;
			} // else skip
		}
	}
	
	// partner meint immer "Identifikation der anderen Person einer Beziehung"
	public static PersonIdentification partner(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		PersonIdentification personIdentification = null;
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(PERSON_IDENTIFICATION) || //
						startName.equals(PERSON_IDENTIFICATION_PARTNER) ||
						startName.equals(PARTNER_ID_ORGANISATION)) personIdentification = StaxEch0044.personIdentification(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				return personIdentification;
			} // else skip
		}
	}
	
	public static void relation(XMLEventReader xml, Person person) throws XMLStreamException, ParserTargetException {
		Relation relation = null;
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(TYPE_OF_RELATIONSHIP) || startName.equals(PARTNERSHIP_TYPE_OF_RELATIONSHIP_TYPE)) {
					String typeOfRelationship = token(xml);
					relation = person.getRelation(typeOfRelationship);
				}
				else if (startName.equals(BASED_ON_LAW)) relation.basedOnLaw = token(xml); 
				else if (startName.equals(CARE)) relation.care = token(xml);
				else if (startName.equals(PARTNER)) relation.partner = partner(xml);
				else if (startName.equals(ADDRESS)) relation.address = StaxEch0010.address(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	// Das ist von der Signatur her zur Zeit eine grosse Ausnahme. Normalerweise würde einfach
	// ein PlaceOfOrigin zurückgeliefert. Hier wird jedoch zusätzlich überprüft, ob ein schon
	// bestehender Eintrag in der Liste ersetzt werden muss.
	public static void addPlaceOfOriginAddon(XMLEventReader xml, List<PlaceOfOrigin> placeOfOrigins) throws XMLStreamException, ParserTargetException {
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(ORIGIN)) StaxEch0011.placeOfOrigin(xml, placeOfOrigin);
				else if (startName.equals(REASON_OF_ACQUISITION)) placeOfOrigin.reasonOfAcquisition = token(xml);
				else if (startName.equals(NATURALIZATION_DATE)) placeOfOrigin.naturalizationDate = StaxEch.date(xml);
				else if (startName.equals(EXPATRIATION_DATE)) placeOfOrigin.expatriationDate = StaxEch.date(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				// eventuell wurden diese Heimatorte schon mit anyPerson von e11 geliefert.
				for (PlaceOfOrigin p : placeOfOrigins) {
					if (StringUtils.equals(placeOfOrigin.originName, p.originName) && StringUtils.equals(placeOfOrigin.canton, p.canton)) {
						placeOfOrigins.remove(p); break;
					}
				}
				placeOfOrigins.add(placeOfOrigin);
				return;
			} // else skip
		}
	}
	
	public static void nameOfParentAtBirth(XMLEventReader xml, Relation relation) throws XMLStreamException, ParserTargetException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String name = element.getName().getLocalPart();
				if (name.equals(FIRST_NAME)) relation.firstNameAtBirth = token(xml);
				else if (name.equals(OFFICIAL_NAME)) relation.officialNameAtBirth = token(xml);
				else skip(xml);
			} else if (event.isEndElement()) return;
			// else skip
		}
	}
	
}
