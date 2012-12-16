package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.COMMERCIAL_REGISTER_INFORMATION;
import static ch.openech.dm.XmlConstants.ORGANISATION;
import static ch.openech.dm.XmlConstants.ORGANISATION_ROOT;
import static ch.openech.dm.XmlConstants.UIDREG_INFORMATION;
import static ch.openech.dm.XmlConstants.UIDREG_SOURCE;
import static ch.openech.dm.XmlConstants.VAT_REGISTER_INFORMATION;
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

	public static Organisation process(String xmlString) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		
		return process(xml);
	}
	
	private static Organisation process(XMLEventReader xml) throws XMLStreamException {
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
	
	private static Organisation organisationRoot(XMLEventReader xml) throws XMLStreamException {
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
	
	public static Organisation organisation(XMLEventReader xml, Organisation organisation) throws XMLStreamException {
		
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
	
	public static void organisationRegistration(XMLEventReader xml, Organisation organisation) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.endsWith("Date")) organisation.set(startName, date(xml));
				else if (startName.equals(UIDREG_SOURCE)) StaxEch0097.uidStructure(xml, organisation.uidregSourceUid);
				else organisation.set(startName, token(xml));
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
}
