package ch.openech.xml.read;

import static  ch.openech.model.XmlConstants.ARRIVAL_DATE;
import static  ch.openech.model.XmlConstants.COMES_FROM;
import static  ch.openech.model.XmlConstants.DATE_OF_DEATH;
import static  ch.openech.model.XmlConstants.DEATH;
import static  ch.openech.model.XmlConstants.DEATH_PERSON;
import static  ch.openech.model.XmlConstants.DELIVERY;
import static  ch.openech.model.XmlConstants.DELIVERY_HEADER;
import static  ch.openech.model.XmlConstants.DEPARTURE_DATE;
import static  ch.openech.model.XmlConstants.DESTINATION_ADDRESS;
import static  ch.openech.model.XmlConstants.DESTINATION_MUNICIPALITY;
import static  ch.openech.model.XmlConstants.DWELLING_ADDRESS;
import static  ch.openech.model.XmlConstants.GOES_TO;
import static  ch.openech.model.XmlConstants.HAS_MAIN_RESIDENCE;
import static  ch.openech.model.XmlConstants.MOVE_IN;
import static  ch.openech.model.XmlConstants.MOVE_IN_PERSON;
import static  ch.openech.model.XmlConstants.MOVE_OUT;
import static  ch.openech.model.XmlConstants.MOVE_OUT_PERSON;
import static  ch.openech.model.XmlConstants.MOVE_OUT_REPORTING_DESTINATION;
import static  ch.openech.model.XmlConstants.NAME_OF_FATHER;
import static  ch.openech.model.XmlConstants.NAME_OF_MOTHER;
import static  ch.openech.model.XmlConstants.OCCUPATION;
import static  ch.openech.model.XmlConstants.RELATIONSHIP;
import static  ch.openech.model.XmlConstants.REPORTING_MUNICIPALITY;
import static  ch.openech.model.XmlConstants.SECONDARY_RESIDENCE;
import static ch.openech.xml.read.StaxEch.date;
import static ch.openech.xml.read.StaxEch.skip;

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import  ch.openech.model.common.DwellingAddress;
import  ch.openech.model.common.Place;
import  ch.openech.model.person.Person;

public class StaxEch0093 {

	private ParserTarget93 parserTarget;
	
	public StaxEch0093(ParserTarget93 parserTarget) {
		this.parserTarget = parserTarget;
	}
	
	public void process(String xmlString) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		
		process(xml, xmlString);
	}

	public void process(InputStream inputStream, String eventString) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
		
		process(xml, eventString);
	}

	private void process(XMLEventReader xml, String xmlString) throws XMLStreamException {
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(DELIVERY)) {
					delivery(xmlString, xml);
				}
				else skip(xml);
			} 
		}
	}
	
	private void delivery(String xmlString, XMLEventReader xml) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(DELIVERY_HEADER)) skip(xml);
				else if (startName.equals(MOVE_IN)) moveIn(xml);
				else if (startName.equals(MOVE_OUT)) moveOut(xml);
				else if (startName.equals(DEATH)) death(xml);
			} else if (event.isEndElement()) return;
			// else skip
		}
	}
	
	//
	
	public void moveIn(XMLEventReader xml) throws XMLStreamException {
		Person person = null;
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(MOVE_IN_PERSON)) person = StaxEch0020.newPerson(xml);
				else if (startName.equals(HAS_MAIN_RESIDENCE)) moveInReportingMunicipality(xml, person);
				else skip(xml);
			} else if (event.isEndElement()) {
				parserTarget.moveIn(person);
				return;
			} // else skip
		}
	}

	private void moveInReportingMunicipality(XMLEventReader xml, Person personToChange) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(REPORTING_MUNICIPALITY)) personToChange.residence.reportingMunicipality = StaxEch0007.municipality(xml);
				else if (startName.equals(ARRIVAL_DATE)) personToChange.arrivalDate = StaxEch.date(xml);
				else if (startName.equals(COMES_FROM)) {
					personToChange.comesFrom = new Place();
					StaxEch0007.municipality(xml, personToChange.comesFrom.municipalityIdentification);
				}
				else if (startName.equals(DWELLING_ADDRESS)) personToChange.dwellingAddress = StaxEch0011.dwellingAddress(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	//
	
	public void moveOut(XMLEventReader xml) throws XMLStreamException {
		Person person = null;
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(MOVE_OUT_PERSON)) person = StaxEch0020.newPerson(xml);
				else if (startName.equals(NAME_OF_FATHER)) StaxEch0021.nameOfParentAtBirth(xml, person.nameOfParents.father);
				else if (startName.equals(NAME_OF_MOTHER)) StaxEch0021.nameOfParentAtBirth(xml, person.nameOfParents.mother);
				else if (startName.equals(RELATIONSHIP) || startName.equals(RELATIONSHIP)) StaxEch0021.relation(xml, person);
				else if (startName.equals(MOVE_OUT_REPORTING_DESTINATION)) moveOutReportingDestination(xml, person);
				else if (startName.equals(SECONDARY_RESIDENCE)) person.residence.secondary.add(StaxEch0007.municipality(xml));
				else if (startName.equals(OCCUPATION)) person.occupation.add(StaxEch0021.occupation(xml));
				else skip(xml);
			} else if (event.isEndElement()) {
				parserTarget.moveOut(person);
				return;
			} // else skip
		}
	}

	private void moveOutReportingDestination(XMLEventReader xml, Person person) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(REPORTING_MUNICIPALITY)) person.residence.reportingMunicipality = StaxEch0007.municipality(xml);
				else if (startName.equals(GOES_TO)) {
					goesTo(xml, person);
				}
				else if (startName.equals(DEPARTURE_DATE)) person.departureDate = StaxEch.date(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	private void goesTo(XMLEventReader xml, Person person) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				// TODO wohin gehört diese Gemeinde?
				if (startName.equals(DESTINATION_MUNICIPALITY)) StaxEch0007.municipality(xml);
				else if (startName.equals(DESTINATION_ADDRESS)) {
					if (person.dwellingAddress == null) person.dwellingAddress = new DwellingAddress();
					person.dwellingAddress.mailAddress = StaxEch0010.address(xml);
				}
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}

	//
	
	public void death(XMLEventReader xml) throws XMLStreamException {
		Person person = null;
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(DEATH_PERSON)) person = StaxEch0020.newPerson(xml);
				else if (startName.equals(DATE_OF_DEATH)) person.dateOfDeath = date(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				parserTarget.death(person);
				return;
			} // else skip
		}
	}
}
