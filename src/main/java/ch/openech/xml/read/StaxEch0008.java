package ch.openech.xml.read;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.model.common.CountryIdentification;

public class StaxEch0008 {

	/*
	 * Die Landinformation besteht nur aus drei einfachen Werten,
	 * die über das generische simpleValue eingelesen werden können. 
	 */
	public static CountryIdentification country(XMLEventReader xml) throws XMLStreamException {
		CountryIdentification countryIdentification = new CountryIdentification();
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if ("countryId".equals(startName)) {
					startName = "id";
				} 
				StaxEch.simpleValue(xml, countryIdentification, startName);
			} else if (event.isEndElement()) return countryIdentification;
			// else skip
		}
	}
	
}
