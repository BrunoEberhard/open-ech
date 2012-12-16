package ch.openech.xml.read;

import static ch.openech.xml.read.StaxEch.integer;
import static ch.openech.xml.read.StaxEch.token;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.mj.db.model.ColumnProperties;
import ch.openech.mj.db.model.Constants;

public class StaxEch0008 {

//	public static CountryIdentification country(XMLEventReader xml) throws XMLStreamException {
//		CountryIdentification countryIdentification = new CountryIdentification();
//		country(xml, countryIdentification);
//		return countryIdentification;
//	}
	
	public static void country(XMLEventReader xml, CountryIdentification countryIdentification) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				
				if (Constants.getProperty(CountryIdentification.COUNTRY_IDENTIFICATION.countryId).getFieldName().equals(startName)) {
					ColumnProperties.setValue(countryIdentification, startName, integer(xml));
				} else {
					ColumnProperties.setValue(countryIdentification, startName, token(xml));
				}
			} else if (event.isEndElement()) return;
			// else skip
		}
	}
	
}
