package ch.openech.xml.read;

import static ch.openech.model.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.model.properties.FlatProperties;
import org.minimalj.util.StringUtils;

import ch.openech.model.person.PersonExtendedInformation;
import ch.openech.model.types.YesNo;

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
				else FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
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
				FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
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
				else FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
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
				else FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	
}
