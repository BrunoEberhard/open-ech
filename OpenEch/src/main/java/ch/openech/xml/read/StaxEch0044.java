package ch.openech.xml.read;

import java.io.IOException;

import org.joda.time.ReadablePartial;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.openech.dm.common.NamedId;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.model.properties.FlatProperties;
import ch.openech.mj.util.StringUtils;

import static ch.openech.xml.read.StaxEch.*;
import static ch.openech.dm.XmlConstants.*;

public class StaxEch0044 {

	public static PersonIdentification personIdentification(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		PersonIdentification personIdentification = new PersonIdentification();
		personIdentification(xmlParser, personIdentification);
		return personIdentification;
	}
		
	public static void personIdentification(XmlPullParser xmlParser, PersonIdentification personIdentification) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				
				if (startName.equals(LOCAL_PERSON_ID)) namedId(xmlParser, personIdentification.technicalIds.localId);
				else if (StringUtils.equals(startName, OTHER_PERSON_ID, OTHER_PERSON_ID)) personIdentification.technicalIds.otherId.add(namedId(xmlParser));
				else if (StringUtils.equals(startName, EU_PERSON_ID, "EuPersonId")) personIdentification.technicalIds.euId.add(namedId(xmlParser));
				
				else if (StringUtils.equals(startName, OFFICIAL_NAME, FIRST_NAME)) {
					FlatProperties.set(personIdentification, startName, token(xmlParser));
				}
				else if (StringUtils.equals(startName, VN)) {
					personIdentification.vn.value = token(xmlParser);
				}
				else if (StringUtils.equals(startName, SEX)) {
					StaxEch.enuum(xmlParser,  personIdentification, PersonIdentification.PERSON_IDENTIFICATION.sex);
				}
				else if (startName.equals(DATE_OF_BIRTH)) {
					FlatProperties.set(personIdentification, startName, datePartiallyKnown(xmlParser));
				}
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	public static NamedId namedId(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
		NamedId namedId = new NamedId();
		namedId(xmlParser, namedId);
		return namedId;
	}
	
	public static void namedId(XmlPullParser xmlParser, NamedId namedId) throws XmlPullParserException, IOException {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				if (xmlParser.getName().equals(PERSON_ID_CATEGORY)) namedId.personIdCategory = token(xmlParser);
				else if (xmlParser.getName().equals(PERSON_ID)) namedId.personId = token(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}

	// also used by 98
	public static ReadablePartial datePartiallyKnown(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		ReadablePartial date = null;
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(YEAR_MONTH_DAY) || startName.equals(YEAR_MONTH) || startName.equals(YEAR)) {
					date = StaxEch.partial(xmlParser);
				} else {
					throw new IllegalArgumentException();
				}
			} else if (isEndTag(event)) {
				// if (date == null) throw new IllegalArgumentException();
				return date;
			} // else skip
		}
	}
	
}
