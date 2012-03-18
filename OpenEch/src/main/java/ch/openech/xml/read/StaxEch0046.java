package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.ADDRESS;
import static ch.openech.dm.XmlConstants.CONTACT;
import static ch.openech.dm.XmlConstants.CONTACT_ROOT;
import static ch.openech.dm.XmlConstants.DATE_FROM;
import static ch.openech.dm.XmlConstants.DATE_TO;
import static ch.openech.dm.XmlConstants.EMAIL;
import static ch.openech.dm.XmlConstants.INTERNET;
import static ch.openech.dm.XmlConstants.LOCAL_I_D;
import static ch.openech.dm.XmlConstants.PHONE;
import static ch.openech.dm.XmlConstants.PHONE_NUMBER;
import static ch.openech.dm.XmlConstants.POSTAL_ADDRESS;
import static ch.openech.dm.XmlConstants.VALIDITY;
import static ch.openech.xml.read.StaxEch.date;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.contact.Contact;
import ch.openech.dm.contact.ContactEntry;

public class StaxEch0046 {

	public Contact read(InputStream inputStream) throws XMLStreamException, ParserTargetException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
		return read(xml);
	}

	
	public Contact read(String xmlString) throws XMLStreamException, ParserTargetException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		return read(xml);
	}

	public static Contact read(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		Contact contact = null;
		 
		while (xml.hasNext()) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(CONTACT_ROOT)) contact = contactRoot(xml);
				else skip(xml);
			} 
		}
		return contact;
	}
	
	public static Contact contactRoot(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		Contact contact = null;
		 
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(CONTACT)) contact = contact(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				return contact;
			} // else skip
		}
	}
	
	public static Contact contact(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		Contact contact = new Contact();
		 
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				
				if (startName.equals(LOCAL_I_D)) contact.stringId = StaxEch0044.namedId(xml).personId;
				else if (startName.equals(ADDRESS)) addContactEntry(contact, xml, "A");
				else if (startName.equals(EMAIL)) addContactEntry(contact, xml, "E");
				else if (startName.equals(PHONE)) addContactEntry(contact, xml, "P");
				else if (startName.equals(INTERNET)) addContactEntry(contact, xml, "I");
				
				else skip(xml);
			} else if (event.isEndElement()) {
				return contact;
			} // else skip
		}
	}
	
	private static void addContactEntry(Contact contact, XMLEventReader xml, String type) throws XMLStreamException, ParserTargetException {
		ContactEntry contactEntry = contactEntry(xml);
		contactEntry.typeOfContact = type;
		contact.entries.add(contactEntry);
	}
	
	private static ContactEntry contactEntry(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		ContactEntry contactEntry = new ContactEntry();
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				boolean category = startName.endsWith("Category");
				boolean value = startName.endsWith("Address") || startName.equals(PHONE_NUMBER);

				if (category) {
					boolean other = startName.startsWith("other");
					if (other) {
						contactEntry.category.other = token(xml);
					} else {
						contactEntry.category.code = token(xml);
					}
				} else if (startName.equals(VALIDITY)) {
					validity(xml, contactEntry);
				} else if (value) {
					if (startName.equals(POSTAL_ADDRESS)) {
						contactEntry.address = StaxEch0010.address(xml);
					} else {
						contactEntry.value = token(xml);
					}
				}
				
				else skip(xml);
			} else if (event.isEndElement()) {
				return contactEntry;
			} // else skip
		}
	}

	private static void validity(XMLEventReader xml, ContactEntry contactEntry) throws XMLStreamException, ParserTargetException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(DATE_FROM)) {
					contactEntry.dateFrom = date(xml);
				} else if (startName.equals(DATE_TO)) {
					contactEntry.dateTo = date(xml);
				} else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
		
}
