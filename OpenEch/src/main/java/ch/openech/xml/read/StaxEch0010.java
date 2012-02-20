package ch.openech.xml.read;

import static ch.openech.xml.read.StaxEch.token;
import static ch.openech.dm.XmlConstants.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.common.Address;
import ch.openech.mj.db.model.ColumnAccess;

public class StaxEch0010 {

	public static Address address(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		Address address = new Address();
		address(address, xml);
		return address;
	}
	
	public static void address(Address address, XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				// Die in der Java - Klasse alle Attribute flach drin sind,
				// kann hier einfach "rekursiv" die Methode wieder aufgerufen werden
				if (startName.equals(ORGANISATION)) address(address, xml);
				else if (startName.equals(PERSON)) address(address, xml);
				else if (startName.equals(ADDRESS_INFORMATION)) address(address, xml);
				else ColumnAccess.setValue(address, startName, token(xml));
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
}
