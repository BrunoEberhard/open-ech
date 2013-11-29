package ch.openech.xml.read;

import java.io.InputStream;

import ch.openech.dm.contact.Contact;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.dm.contact.ContactEntryType;

public class StaxEch0046 {

	public Contact read(InputStream inputStream)  {
		//XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		//XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
		return read();
	}

	
	public Contact read(String xmlString) {
		//XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		//XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		return read();
	}

	public static Contact read()  {
		Contact contact = null;
		 
//		while (xml.hasNext()) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(CONTACT_ROOT)) contact = contactRoot(xml);
//				else skip(xml);
//			} 
//		}
		return contact;
	}
	
	public static Contact contactRoot()  {
		Contact contact = null;
		return contact;
		 
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(CONTACT)) contact = contact(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return contact;
//			} // else skip
//		}
	}
	
	public static Contact contact()  {
		Contact contact = new Contact();
		 return contact;
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				
//				if (startName.equals(LOCAL_I_D)) contact.stringId = StaxEch0044.namedId(xml).personId;
//				else if (startName.equals(ADDRESS)) addContactEntry(contact, xml, ContactEntryType.Address);
//				else if (startName.equals(EMAIL)) addContactEntry(contact, xml, ContactEntryType.Email);
//				else if (startName.equals(PHONE)) addContactEntry(contact, xml, ContactEntryType.Phone);
//				else if (startName.equals(INTERNET)) addContactEntry(contact, xml, ContactEntryType.Internet);
//				
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return contact;
//			} // else skip
//		}
	}
	
	private static void addContactEntry(Contact contact, ContactEntryType type)  {
		//ContactEntry contactEntry = contactEntry(xml);
		// contactEntry.typeOfContact = type;
		contact.entries.add(new ContactEntry());
	}
	
	private static ContactEntry contactEntry()  {
		ContactEntry contactEntry = new ContactEntry();
		return contactEntry;
		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				boolean category = startName.endsWith("Category");
//				boolean phone = startName.equals(PHONE_NUMBER);
//				boolean value = startName.endsWith("Address") || phone;
//
//				if (category) {
//					boolean other = startName.startsWith("other");
//					if (other) {
//						if (phone) {
//							contactEntry.phoneCategoryOther = token(xml);
//						} else {
//							contactEntry.categoryOther = token(xml);
//						}						
//					} else {
//						if (phone) {
//							StaxEch.enuum(xml, contactEntry, ContactEntry.CONTACT_ENTRY.phoneCategory);
//						} else {
//							StaxEch.enuum(xml, contactEntry, ContactEntry.CONTACT_ENTRY.categoryCode);
//						}
//					}
//				} else if (startName.equals(VALIDITY)) {
//					validity(xml, contactEntry);
//				} else if (value) {
//					if (startName.equals(POSTAL_ADDRESS)) {
//						contactEntry.address = StaxEch0010.address(xml);
//					} else {
//						contactEntry.value = token(xml);
//					}
//				}
//				
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return contactEntry;
//			} // else skip
//		}
	}

	private static void validity(ContactEntry contactEntry)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(DATE_FROM)) {
//					contactEntry.dateFrom = date(xml);
//				} else if (startName.equals(DATE_TO)) {
//					contactEntry.dateTo = date(xml);
//				} else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
		
}
