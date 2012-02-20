package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.DATE_OF_BIRTH;
import static ch.openech.dm.XmlConstants.EU_PERSON_ID;
import static ch.openech.dm.XmlConstants.FIRST_NAME;
import static ch.openech.dm.XmlConstants.LOCAL_PERSON_ID;
import static ch.openech.dm.XmlConstants.OFFICIAL_NAME;
import static ch.openech.dm.XmlConstants.OTHER_PERSON_ID;
import static ch.openech.dm.XmlConstants.PERSON_ID;
import static ch.openech.dm.XmlConstants.PERSON_ID_CATEGORY;
import static ch.openech.dm.XmlConstants.SEX;
import static ch.openech.dm.XmlConstants.VN;
import static ch.openech.dm.XmlConstants.YEAR;
import static ch.openech.dm.XmlConstants.YEAR_MONTH;
import static ch.openech.dm.XmlConstants.YEAR_MONTH_DAY;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.common.NamedId;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.db.model.ColumnAccess;
import ch.openech.mj.util.StringUtils;

public class StaxEch0044 {

	public static PersonIdentification personIdentification(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		PersonIdentification personIdentification = new PersonIdentification();
		personIdentification(xml, personIdentification);
		return personIdentification;
	}
		
	public static void personIdentification(XMLEventReader xml, PersonIdentification personIdentification) throws XMLStreamException, ParserTargetException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				
				if (startName.equals(LOCAL_PERSON_ID)) namedId(xml, personIdentification.technicalIds.localId);
				else if (StringUtils.equals(startName, OTHER_PERSON_ID, OTHER_PERSON_ID)) personIdentification.technicalIds.otherId.add(namedId(xml));
				else if (StringUtils.equals(startName, EU_PERSON_ID, "EuPersonId")) personIdentification.technicalIds.euId.add(namedId(xml));
				
				else if (StringUtils.equals(startName, VN, OFFICIAL_NAME, FIRST_NAME, SEX)) {
					ColumnAccess.setValue(personIdentification, startName, token(xml));
				}
				else if (startName.equals(DATE_OF_BIRTH)) {
					ColumnAccess.setValue(personIdentification, startName, datePartiallyKnown(xml));
				}
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	public static int namedIdOpenEch(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		NamedId namedId = namedId(xml);
		if ("CH.OPENECH".equals(namedId.personIdCategory)) {
			return Integer.valueOf(namedId.personId);
		} else {
			return 0;
		}
	}
	
	public static NamedId namedId(XMLEventReader xml) throws XMLStreamException, ParserTargetException {
		NamedId namedId = new NamedId();
		namedId(xml, namedId);
		return namedId;
	}
	
	public static void namedId(XMLEventReader xml, NamedId namedId) throws XMLStreamException, ParserTargetException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				if (startElement.getName().getLocalPart().equals(PERSON_ID_CATEGORY)) namedId.personIdCategory = token(xml);
				else if (startElement.getName().getLocalPart().equals(PERSON_ID)) namedId.personId = token(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}

	// also used by 98
	public static String datePartiallyKnown(XMLEventReader xml) throws XMLStreamException {
		String date = null;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(YEAR_MONTH_DAY) || startName.equals(YEAR_MONTH) || startName.equals(YEAR)) {
					date = StaxEch.date(xml);
				} else {
					throw new IllegalArgumentException();
				}
			} else if (event.isEndElement()) {
				// if (date == null) throw new IllegalArgumentException();
				return date;
			} // else skip
		}
	}
	
}
