package ch.openech.xml.read;

import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import ch.openech.dm.person.Person;

public class StaxEch0093 {

	private ParserTarget93 parserTarget;
	
	public StaxEch0093(ParserTarget93 parserTarget) {
		this.parserTarget = parserTarget;
	}
	
	public void process(String xmlString)  {
//		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	//	XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		
		process(xmlString);
	}

	public void process(InputStream inputStream, String eventString)  {
//		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
		
		process(eventString);
	}

	//private void process(String xmlString)  {
//		while (xml.hasNext()) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(DELIVERY)) {
//					delivery(xmlString, xml);
//				}
//				else skip(xml);
//			} 
//		}
//	}
	
	private void delivery(String xmlString)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(DELIVERY_HEADER)) skip(xml);
//				else if (startName.equals(MOVE_IN)) moveIn(xml);
//				else if (startName.equals(MOVE_OUT)) moveOut(xml);
//				else if (startName.equals(DEATH)) death(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
	//
	
	public void moveIn() throws XMLStreamException {
//		Person person = null;
//		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(MOVE_IN_PERSON)) person = StaxEch0020.newPerson(xml);
//				else if (startName.equals(HAS_MAIN_RESIDENCE)) moveInReportingMunicipality(xml, person);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				parserTarget.moveIn(person);
//				return;
//			} // else skip
//		}
	}

	private void moveInReportingMunicipality(Person personToChange)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(REPORTING_MUNICIPALITY)) personToChange.residence.reportingMunicipality = StaxEch0007.municipality(xml);
//				else if (startName.equals(ARRIVAL_DATE)) personToChange.arrivalDate = StaxEch.date(xml);
//				else if (startName.equals(COMES_FROM)) {
//					personToChange.comesFrom = new Place();
//					StaxEch0007.municipality(xml, personToChange.comesFrom.municipalityIdentification);
//				}
//				else if (startName.equals(DWELLING_ADDRESS)) personToChange.dwellingAddress = StaxEch0011.dwellingAddress(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
	//
	
	public void moveOut()  {
		Person person = null;
		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(MOVE_OUT_PERSON)) person = StaxEch0020.newPerson(xml);
//				else if (startName.equals(NAME_OF_FATHER)) StaxEch0021.nameOfParentAtBirth(xml, person.nameOfParents.father);
//				else if (startName.equals(NAME_OF_MOTHER)) StaxEch0021.nameOfParentAtBirth(xml, person.nameOfParents.mother);
//				else if (startName.equals(RELATIONSHIP) || startName.equals(RELATIONSHIP)) StaxEch0021.relation(xml, person);
//				else if (startName.equals(MOVE_OUT_REPORTING_DESTINATION)) moveOutReportingDestination(xml, person);
//				else if (startName.equals(SECONDARY_RESIDENCE)) person.residence.secondary.add(StaxEch0007.municipality(xml));
//				else if (startName.equals(OCCUPATION)) person.occupation.add(StaxEch0021.occupation(xml));
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				parserTarget.moveOut(person);
//				return;
//			} // else skip
//		}
	}

	private void moveOutReportingDestination(Person person)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(REPORTING_MUNICIPALITY)) person.residence.reportingMunicipality = StaxEch0007.municipality(xml);
//				else if (startName.equals(GOES_TO)) {
//					goesTo(xml, person);
//				}
//				else if (startName.equals(DEPARTURE_DATE)) person.departureDate = StaxEch.date(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
	private void goesTo(Person person)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				// TODO wohin gehört diese Gemeinde?
//				if (startName.equals(DESTINATION_MUNICIPALITY)) StaxEch0007.municipality(xml);
//				else if (startName.equals(DESTINATION_ADDRESS)) {
//					if (person.dwellingAddress == null) person.dwellingAddress = new DwellingAddress();
//					person.dwellingAddress.mailAddress = StaxEch0010.address(xml);
//				}
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}

	//
	
	public void death()  {
//		Person person = null;
//		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(DEATH_PERSON)) person = StaxEch0020.newPerson(xml);
//				else if (startName.equals(DATE_OF_DEATH)) person.dateOfDeath = date(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				parserTarget.death(person);
//				return;
//			} // else skip
//		}
	}
}
