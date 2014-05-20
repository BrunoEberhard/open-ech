package ch.openech.xml.read;

import static  ch.openech.model.XmlConstants.ADDRESS_INFORMATION;
import static  ch.openech.model.XmlConstants.FOREIGN_ZIP_CODE;
import static  ch.openech.model.XmlConstants.ORGANISATION;
import static  ch.openech.model.XmlConstants.PERSON;
import static  ch.openech.model.XmlConstants.SWISS_ZIP_CODE;
import static  ch.openech.model.XmlConstants.SWISS_ZIP_CODE_ADD_ON;
import static  ch.openech.model.XmlConstants.SWISS_ZIP_CODE_ID;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import  ch.openech.model.common.Address;

public class StaxEch0010 {

	public static Address address(XMLEventReader xml) throws XMLStreamException {
		Address address = new Address();
		address(address, xml);
		return address;
	}
	
	public static void address(Address address, XMLEventReader xml) throws XMLStreamException {
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
				else if (startName.equals(SWISS_ZIP_CODE)) address.zip = StaxEch.token(xml);
				else if (startName.equals(FOREIGN_ZIP_CODE)) address.zip = StaxEch.token(xml);
				else if (startName.equals(SWISS_ZIP_CODE_ID)) address.swissZipCodeId = StaxEch.integer(xml);
				else if (startName.equals(SWISS_ZIP_CODE_ADD_ON)) address.zip += " " + StaxEch.token(xml);
				else StaxEch.simpleValue(xml, address, startName);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
}
