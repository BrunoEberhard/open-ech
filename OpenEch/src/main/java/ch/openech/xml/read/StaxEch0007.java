package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.CANTON_ABBREVIATION;
import static ch.openech.dm.XmlConstants.HISTORY_MUNICIPALITY_ID;
import static ch.openech.dm.XmlConstants.HISTORY_MUNICIPIALITY_ID;
import static ch.openech.dm.XmlConstants.MUNICIPALITY_ID;
import static ch.openech.dm.XmlConstants.MUNICIPALITY_NAME;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.common.MunicipalityIdentification;

public class StaxEch0007 {

	public static MunicipalityIdentification municipality(XMLEventReader xml) throws XMLStreamException {
		MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
		municipality(xml, municipalityIdentification);
		return municipalityIdentification;
	}
		
	public static void municipality(XMLEventReader xml, MunicipalityIdentification municipalityIdentification) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(MUNICIPALITY_ID)) municipalityIdentification.municipalityId = StaxEch.integer(xml);
				else if (startName.equals(MUNICIPALITY_NAME)) municipalityIdentification.municipalityName = token(xml);
				else if (startName.equals(CANTON_ABBREVIATION)) municipalityIdentification.cantonAbbreviation.canton = token(xml);
				else if (startName.equals(HISTORY_MUNICIPIALITY_ID) || startName.equals(HISTORY_MUNICIPALITY_ID)) municipalityIdentification.historyMunicipalityId = StaxEch.integer(xml);
				else skip(xml);
			} else if (event.isEndElement()) return;
			// else skip
		}
	}
	
}
