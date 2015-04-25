package ch.openech.xml.read;

import static ch.openech.model.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.util.EqualsHelper;
import org.minimalj.util.StringUtils;

import ch.openech.model.person.NameOfParent;
import ch.openech.model.person.Occupation;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.model.person.Relation;
import ch.openech.model.person.types.TypeOfRelationship;
import ch.openech.model.types.YesNo;

public class StaxEch0021 {

	public static Occupation occupation(XMLEventReader xml) throws XMLStreamException {
		Occupation occupation = new Occupation();
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(JOB_TITLE)) occupation.jobTitle = token(xml);
				else if (startName.equals(KIND_OF_EMPLOYMENT)) enuum(xml, occupation, Occupation.$.kindOfEmployment);
				else if (startName.equals(EMPLOYER)) occupation.employer = token(xml);
				else if (startName.equals(PLACE_OF_WORK)) occupation.placeOfWork = StaxEch0010.address(xml);
				else if (startName.equals(PLACE_OF_EMPLOYER)) occupation.placeOfEmployer = StaxEch0010.address(xml);
				else if (startName.equals(OCCUPATION_VALID_TILL)) occupation.occupationValidTill = date(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				return occupation;
			} // else skip
		}
	}
	
	// partner meint immer "Identifikation der anderen Person einer Beziehung"
	public static PersonIdentification partner(XMLEventReader xml) throws XMLStreamException {
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
	
	public static void relation(XMLEventReader xml, Person person) throws XMLStreamException {
		Relation relation = null;
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(TYPE_OF_RELATIONSHIP) || startName.equals(PARTNERSHIP_TYPE_OF_RELATIONSHIP_TYPE)) {
					relation = new Relation();
					relation.typeOfRelationship = StaxEch.enuum(TypeOfRelationship.class, token(xml));
					if (relation.isParent()) relation.care = YesNo.Yes;
					person.relation.add(relation);
				}
				else if (startName.equals(BASED_ON_LAW)) enuum(xml, relation, Relation.$.basedOnLaw); 
				else if (startName.equals(BASED_ON_LAW_ADD_ON)) relation.basedOnLawAddOn = token(xml);
				else if (startName.equals(CARE)) enuum(xml, relation, Relation.$.care);
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
	public static void addPlaceOfOriginAddon(XMLEventReader xml, List<PlaceOfOrigin> placeOfOrigins) throws XMLStreamException {
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(ORIGIN)) StaxEch0011.placeOfOrigin(xml, placeOfOrigin);
				else if (startName.equals(REASON_OF_ACQUISITION)) enuum(xml, placeOfOrigin, PlaceOfOrigin.$.reasonOfAcquisition);
				else if (startName.equals(NATURALIZATION_DATE)) placeOfOrigin.naturalizationDate = StaxEch.date(xml);
				else if (startName.equals(EXPATRIATION_DATE)) placeOfOrigin.expatriationDate = StaxEch.date(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				// eventuell wurden diese Heimatorte schon mit anyPerson von e11 geliefert.
				updatePlaceOfOrigin(placeOfOrigins, placeOfOrigin);
				return;
			} // else skip
		}
	}
	
	public static void updatePlaceOfOrigin(List<PlaceOfOrigin> placeOfOrigins, PlaceOfOrigin placeOfOrigin) {
		for (PlaceOfOrigin p : placeOfOrigins) {
			if (StringUtils.equals(placeOfOrigin.originName, p.originName) && EqualsHelper.equals(placeOfOrigin.canton, p.canton)) {
				placeOfOrigins.remove(p); break;
			}
		}
		placeOfOrigins.add(placeOfOrigin);
	}
	
	public static void nameOfParentAtBirth(XMLEventReader xml, NameOfParent nameAtBirth) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String name = element.getName().getLocalPart();
				if (name.equals(FIRST_NAME)) nameAtBirth.firstName = token(xml);
				else if (name.equals(OFFICIAL_NAME)) nameAtBirth.officialName = token(xml);
				else skip(xml);
			} else if (event.isEndElement()) return;
			// else skip
		}
	}
	
}
