package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.ARMED_FORCES;
import static ch.openech.dm.XmlConstants.FIRE_SERVICE;
import static ch.openech.dm.XmlConstants.HEALTH_INSURANCE;
import static ch.openech.dm.XmlConstants.INSURANCE;
import static ch.openech.dm.XmlConstants.INSURANCE_ADDRESS;
import static ch.openech.dm.XmlConstants.PERSON_ADDON;
import static ch.openech.dm.XmlConstants.PERSON_EXTENDED_INFORMATION_ROOT;
import static ch.openech.dm.XmlConstants.PERSON_IDENTIFICATION;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.person.PersonExtendedInformation;
import ch.openech.dm.types.YesNo;
import ch.openech.mj.db.model.ColumnProperties;
import ch.openech.mj.util.StringUtils;

public class StaxEch0101 {

	public PersonExtendedInformation read(InputStream inputStream) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
		return read(xml);
	}

	public PersonExtendedInformation read(String xmlString) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		return read(xml);
	}

	public static PersonExtendedInformation read(XMLEventReader xml) throws XMLStreamException {
		PersonExtendedInformation information = null;
		 
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(PERSON_EXTENDED_INFORMATION_ROOT)) information = root(xml);
				else skip(xml);
			} 
		}
		return information;
	}
	
	public static PersonExtendedInformation root(XMLEventReader xml) throws XMLStreamException {
		PersonExtendedInformation information = new PersonExtendedInformation();
		 
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(PERSON_ADDON)) information(information, xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				return information;
			} // else skip
		}
	}

	public static PersonExtendedInformation information(XMLEventReader xml) throws XMLStreamException {
		PersonExtendedInformation information = new PersonExtendedInformation();
		information(information, xml);
		return information;
	}
	
	public static void information(PersonExtendedInformation information, XMLEventReader xml) throws XMLStreamException {
		 
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				
				if (startName.equals(HEALTH_INSURANCE)) healthInsurance(information, xml);
				else if (StringUtils.equals(startName, ARMED_FORCES, FIRE_SERVICE, HEALTH_INSURANCE)) informationItem(information, xml);
				else if (startName.equalsIgnoreCase(PERSON_IDENTIFICATION)) StaxEch0044.personIdentification(xml);
				else ColumnProperties.setValue(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	public static void informationItem(PersonExtendedInformation information, XMLEventReader xml) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				ColumnProperties.setValue(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	private static void healthInsurance(PersonExtendedInformation information, XMLEventReader xml) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				
				if (startName.equals(INSURANCE)) insurance(information, xml);
				else ColumnProperties.setValue(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}

	private static void insurance(PersonExtendedInformation information, XMLEventReader xml) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(INSURANCE_ADDRESS)) information.insuranceAddress = StaxEch0010.address(xml);
				else ColumnProperties.setValue(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	
}
