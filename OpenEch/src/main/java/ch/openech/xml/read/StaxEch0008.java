package ch.openech.xml.read;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.common.CountryIdentification;

public class StaxEch0008 {

	/*
	 * Die Landinformation besteht nur aus drei einfachen Werten,
	 * die über das generische simpleValue eingelesen werden können. 
	 */
	public static void country(XMLEventReader xml, CountryIdentification countryIdentification) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				StaxEch.simpleValue(xml, countryIdentification, startName);
			} else if (event.isEndElement()) return;
			// else skip
		}
	}
	
}
