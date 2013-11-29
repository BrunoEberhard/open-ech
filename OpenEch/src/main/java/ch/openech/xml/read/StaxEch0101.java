package ch.openech.xml.read;

import java.io.InputStream;

import ch.openech.dm.person.PersonExtendedInformation;

public class StaxEch0101 {

	public PersonExtendedInformation read(InputStream inputStream)  {
//		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
//		return read(xml);
		return read();
	}

	public PersonExtendedInformation read(String xmlString)  {
//		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//		XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		return read();
	}

	public static PersonExtendedInformation read()  {
		PersonExtendedInformation information = null;
		 
//		while (xml.hasNext()) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(PERSON_EXTENDED_INFORMATION_ROOT)) information = root(xml);
//				else skip(xml);
//			} 
//		}
		return information;
	}
	
	public static PersonExtendedInformation root()  {
		PersonExtendedInformation information = new PersonExtendedInformation();
		 return information;
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(PERSON_ADDON)) information(information, xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return information;
//			} // else skip
//		}
	}

	public static PersonExtendedInformation information()  {
		PersonExtendedInformation information = new PersonExtendedInformation();
		// information(information, xml);
		return information;
	}
	
	public static void information(PersonExtendedInformation information)  {
		 
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				
//				if (startName.equals(HEALTH_INSURANCE)) healthInsurance(information, xml);
//				else if (StringUtils.equals(startName, ARMED_FORCES, FIRE_SERVICE, HEALTH_INSURANCE)) informationItem(information, xml);
//				else if (startName.equalsIgnoreCase(PERSON_IDENTIFICATION)) StaxEch0044.personIdentification(xml);
//				else FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
	public static void informationItem(PersonExtendedInformation information)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
	private static void healthInsurance(PersonExtendedInformation information)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				
//				if (startName.equals(INSURANCE)) insurance(information, xml);
//				else FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}

	private static void insurance(PersonExtendedInformation information)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(INSURANCE_ADDRESS)) information.insuranceAddress = StaxEch0010.address(xml);
//				else FlatProperties.set(information, startName, StaxEch.enuum(YesNo.class, token(xml)));
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
	
}
