package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.contact.Contact;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.dm.contact.ContactEntryType;

public class StaxEch0046 {

	public Contact read(InputStream inputStream) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
		return read(xml);
	}

	
	public Contact read(String xmlString) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		return read(xml);
	}

	public static Contact read(XMLEventReader xml) throws XMLStreamException {
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
	
	public static Contact contactRoot(XMLEventReader xml) throws XMLStreamException {
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
	
	public static void contact(XMLEventReader xml, List<ContactEntry> contacts) throws XMLStreamException {
		Contact contact = contact(xml);
		contacts.clear();
		contacts.addAll(contact.entries);
	}
	
	public static Contact contact(XMLEventReader xml) throws XMLStreamException {
		Contact contact = new Contact();
		 
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				
				if (startName.equals(LOCAL_I_D)) contact.stringId = StaxEch0044.namedId(xml).personId;
				else if (startName.equals(ADDRESS)) addContactEntry(contact, xml, ContactEntryType.Address);
				else if (startName.equals(EMAIL)) addContactEntry(contact, xml, ContactEntryType.Email);
				else if (startName.equals(PHONE)) addContactEntry(contact, xml, ContactEntryType.Phone);
				else if (startName.equals(INTERNET)) addContactEntry(contact, xml, ContactEntryType.Internet);
				
				else skip(xml);
			} else if (event.isEndElement()) {
				return contact;
			} // else skip
		}
	}
	
	private static void addContactEntry(Contact contact, XMLEventReader xml, ContactEntryType type) throws XMLStreamException {
		ContactEntry contactEntry = contactEntry(xml);
		contactEntry.typeOfContact = type;
		contact.entries.add(contactEntry);
	}
	
	private static ContactEntry contactEntry(XMLEventReader xml) throws XMLStreamException {
		ContactEntry contactEntry = new ContactEntry();
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				boolean category = startName.endsWith("Category");
				boolean phone = startName.equals(PHONE_NUMBER);
				boolean value = startName.endsWith("Address") || phone;

				if (category) {
					boolean other = startName.startsWith("other");
					if (other) {
						if (phone) {
							contactEntry.phoneCategoryOther = token(xml);
						} else {
							contactEntry.categoryOther = token(xml);
						}						
					} else {
						if (phone) {
							StaxEch.enuum(xml, contactEntry, ContactEntry.CONTACT_ENTRY.phoneCategory);
						} else {
							StaxEch.enuum(xml, contactEntry, ContactEntry.CONTACT_ENTRY.categoryCode);
						}
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

	private static void validity(XMLEventReader xml, ContactEntry contactEntry) throws XMLStreamException {
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
