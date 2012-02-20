package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.*;

import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.util.StringUtils;

public class StaxEch0098 {

	public static void organisation(XMLEventReader xml, Organisation organisation) throws XMLStreamException, ParserTargetException {
		 
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(ORGANISATION_IDENTIFICATION)) StaxEch0097.organisationIdentification(xml, organisation);
				else if (StringUtils.equals(startName, FOUNDATION, LIQUIDATION)) foundationOrLiquidation(xml, organisation);
				else if (StringUtils.equals(startName, UID_BRANCHE_TEXT, NOGA_CODE, LANGUAGE_OF_CORRESPONDANCE)) organisation.set(startName, token(xml));
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	private static void foundationOrLiquidation(XMLEventReader xml, Organisation organisation) throws XMLStreamException, ParserTargetException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (StringUtils.equals(startName,  FOUNDATION_REASON,  LIQUIDATION_REASON)) organisation.set(startName, token(xml));
				else if (StringUtils.equals(startName, FOUNDATION_DATE,  LIQUIDATION_DATE)) organisation.set(startName, StaxEch0044.datePartiallyKnown(xml));
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}

}
