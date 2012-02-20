package ch.openech.xml.read;

import static ch.openech.xml.read.StaxEch.token;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.mj.db.model.ColumnAccess;

public class StaxEch0008 {

//	public static CountryIdentification country(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
//		CountryIdentification countryIdentification = new CountryIdentification();
//		country(xml, countryIdentification);
//		return countryIdentification;
//	}
	
	public static void country(XMLEventReader xml, CountryIdentification countryIdentification) throws XMLStreamException, ParserTargetException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();

				ColumnAccess.setValue(countryIdentification, startName, token(xml));
			} else if (event.isEndElement()) return;
			// else skip
		}
	}
	
}
