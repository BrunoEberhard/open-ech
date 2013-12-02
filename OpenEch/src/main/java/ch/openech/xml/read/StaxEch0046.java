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
import static ch.openech.xml.read.StaxEch.isEndDocument;
import static ch.openech.xml.read.StaxEch.isEndTag;
import static ch.openech.xml.read.StaxEch.isStartTag;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import ch.openech.dm.contact.Contact;
import ch.openech.dm.contact.ContactEntry;
import ch.openech.dm.contact.ContactEntryType;

public class StaxEch0046 {

	public Contact read(InputStream inputStream) throws XmlPullParserException, IOException  {
		return read(new InputStreamReader(inputStream));
	}
	
	public Contact read(String xmlString) throws XmlPullParserException, IOException {
		return read(new StringReader(xmlString));
	}

	private Contact read(Reader reader) throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xmlParser = factory.newPullParser();
		xmlParser.setInput(reader);
		return read(xmlParser);
	}


	public static Contact read(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		Contact contact = null;
		while ( !isEndDocument(xmlParser.getEventType()) ) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(CONTACT_ROOT)) contact = contactRoot(xmlParser);
				else skip(xmlParser);
			} 
		}
		return contact;
	}
	
	public static Contact contactRoot(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		Contact contact = null;
		 
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(CONTACT)) contact = contact(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return contact;
			} // else skip
		}
	}
	
	public static Contact contact(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		Contact contact = new Contact();
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				
				if (startName.equals(LOCAL_I_D)) contact.stringId = StaxEch0044.namedId(xmlParser).personId;
				else if (startName.equals(ADDRESS)) addContactEntry(contact, xmlParser, ContactEntryType.Address);
				else if (startName.equals(EMAIL)) addContactEntry(contact, xmlParser, ContactEntryType.Email);
				else if (startName.equals(PHONE)) addContactEntry(contact, xmlParser, ContactEntryType.Phone);
				else if (startName.equals(INTERNET)) addContactEntry(contact, xmlParser, ContactEntryType.Internet);
				
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return contact;
			} // else skip
		}
	}
	
	private static void addContactEntry(Contact contact,XmlPullParser xmlParser, ContactEntryType type) throws XmlPullParserException, IOException  {
		ContactEntry contactEntry = contactEntry(xmlParser);
		 contactEntry.typeOfContact = type;
		contact.entries.add(new ContactEntry());
	}
	
	private static ContactEntry contactEntry(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		ContactEntry contactEntry = new ContactEntry();
		
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				boolean category = startName.endsWith("Category");
				boolean phone = startName.equals(PHONE_NUMBER);
				boolean value = startName.endsWith("Address") || phone;

				if (category) {
					boolean other = startName.startsWith("other");
					if (other) {
						if (phone) {
							contactEntry.phoneCategoryOther = token(xmlParser);
						} else {
							contactEntry.categoryOther = token(xmlParser);
						}						
					} else {
						if (phone) {
							StaxEch.enuum(xmlParser, contactEntry, ContactEntry.CONTACT_ENTRY.phoneCategory);
						} else {
							StaxEch.enuum(xmlParser, contactEntry, ContactEntry.CONTACT_ENTRY.categoryCode);
						}
					}
				} else if (startName.equals(VALIDITY)) {
					validity(xmlParser, contactEntry);
				} else if (value) {
					if (startName.equals(POSTAL_ADDRESS)) {
						contactEntry.address = StaxEch0010.address(xmlParser);
					} else {
						contactEntry.value = token(xmlParser);
					}
				}
				
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return contactEntry;
			} // else skip
		}
	}

	private static void validity(XmlPullParser xmlParser, ContactEntry contactEntry) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(DATE_FROM)) {
					contactEntry.dateFrom = date(xmlParser);
				} else if (startName.equals(DATE_TO)) {
					contactEntry.dateTo = date(xmlParser);
				} else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
		
}
