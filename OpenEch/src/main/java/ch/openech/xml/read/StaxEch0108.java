package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.date;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.util.StringUtils;

public class StaxEch0108 {

	public static Organisation process(String xmlString) throws XMLStreamException, ParserTargetException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		
		return process(xml);
	}
	
	private static Organisation process(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		Organisation organisation = null;
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(ORGANISATION_ROOT)) {
					organisation = organisationRoot(xml);
				}
				else skip(xml);
			} 
		}
		return organisation;
	}
	
	private static Organisation organisationRoot(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		Organisation organisation = new Organisation(); 

		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(ORGANISATION)) {
					organisation = organisation(xml, organisation);
				}
				else skip(xml);
			} 
		}
		return organisation;
	}
	
	public static Organisation organisation(XMLEventReader xml, Organisation organisation) throws XMLStreamException, ParserTargetException {
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				
				if (startName.equals(ORGANISATION)) StaxEch0098.organisation(xml, organisation);
				else if (StringUtils.equals(startName, UIDREG_INFORMATION, COMMERCIAL_REGISTER_INFORMATION, VAT_REGISTER_INFORMATION)) organisationRegistration(xml, organisation);
				else skip(xml);
			} else if (event.isEndElement()) {
				return organisation;
			} // else skip
		}
	}
	
	private static void organisationRegistration(XMLEventReader xml, Organisation organisation) throws XMLStreamException, ParserTargetException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.endsWith("Date")) organisation.set(startName, date(xml));
				else organisation.set(startName, token(xml));
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
}
