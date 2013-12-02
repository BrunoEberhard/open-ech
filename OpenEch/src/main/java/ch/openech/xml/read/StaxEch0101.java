package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.ARMED_FORCES;
import static ch.openech.dm.XmlConstants.FIRE_SERVICE;
import static ch.openech.dm.XmlConstants.HEALTH_INSURANCE;
import static ch.openech.dm.XmlConstants.INSURANCE;
import static ch.openech.dm.XmlConstants.INSURANCE_ADDRESS;
import static ch.openech.dm.XmlConstants.PERSON_ADDON;
import static ch.openech.dm.XmlConstants.PERSON_EXTENDED_INFORMATION_ROOT;
import static ch.openech.dm.XmlConstants.PERSON_IDENTIFICATION;
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

import ch.openech.dm.person.PersonExtendedInformation;
import ch.openech.dm.types.YesNo;
import ch.openech.mj.model.properties.FlatProperties;
import ch.openech.mj.util.StringUtils;

public class StaxEch0101 {

	public PersonExtendedInformation read(InputStream inputStream) throws XmlPullParserException, IOException  {
		return read(new InputStreamReader(inputStream));
	}

	public PersonExtendedInformation read(String xmlString) throws XmlPullParserException, IOException  {
		return read(new StringReader(xmlString));
	}
	
	public PersonExtendedInformation read(Reader reader) throws XmlPullParserException, IOException  {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xmlParser = factory.newPullParser();
		xmlParser.setInput(reader);
		return read(xmlParser);
	}
	
	

	public static PersonExtendedInformation read(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		PersonExtendedInformation information = null;
		 
		while (!isEndDocument(xmlParser.getEventType())) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(PERSON_EXTENDED_INFORMATION_ROOT)) information = root(xmlParser);
				else skip(xmlParser);
			} 
		}
		return information;
	}
	
	public static PersonExtendedInformation root(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		PersonExtendedInformation information = new PersonExtendedInformation();
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(PERSON_ADDON)) information(information, xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return information;
			} // else skip
		}
	}

	public static PersonExtendedInformation information(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		PersonExtendedInformation information = new PersonExtendedInformation();
		information(information, xmlParser);
		return information;
	}
	
	public static void information(PersonExtendedInformation information, XmlPullParser xmlPaser) throws XmlPullParserException, IOException  {
		 
		while (true) {
			int event = xmlPaser.next();
			if (isStartTag(event)) {
				String startName = xmlPaser.getName();
				if (startName.equals(HEALTH_INSURANCE)) healthInsurance(information, xmlPaser);
				else if (StringUtils.equals(startName, ARMED_FORCES, FIRE_SERVICE, HEALTH_INSURANCE)) informationItem(information, xmlPaser);
				else if (startName.equalsIgnoreCase(PERSON_IDENTIFICATION)) StaxEch0044.personIdentification(xmlPaser);
				else FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xmlPaser)));
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	public static void informationItem(PersonExtendedInformation information, XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xmlParser)));
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	private static void healthInsurance(PersonExtendedInformation information, XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				
				if (startName.equals(INSURANCE)) insurance(information, xmlParser);
				else FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xmlParser)));
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}

	private static void insurance(PersonExtendedInformation information, XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(INSURANCE_ADDRESS)) information.insuranceAddress = StaxEch0010.address(xmlParser);
				else FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xmlParser)));
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	
}
