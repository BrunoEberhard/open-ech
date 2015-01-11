package ch.openech.xml.read;

import static ch.openech.model.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.model.common.Canton;
import ch.openech.model.common.MunicipalityIdentification;

public class StaxEch0007 {

	public static MunicipalityIdentification municipality(XMLEventReader xml) throws XMLStreamException {
		MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(MUNICIPALITY_ID)) municipalityIdentification.id = StaxEch.integer(xml);
				else if (startName.equals(MUNICIPALITY_NAME)) municipalityIdentification.municipalityName = token(xml);
				else if (startName.equals(CANTON_ABBREVIATION)) municipalityIdentification.canton = new Canton(token(xml));
				else if (startName.equals(HISTORY_MUNICIPIALITY_ID) || startName.equals(HISTORY_MUNICIPALITY_ID)) municipalityIdentification.historyMunicipalityId = StaxEch.integer(xml);
				else skip(xml);
			} else if (event.isEndElement()) return municipalityIdentification;
			// else skip
		}
	}
	
}
