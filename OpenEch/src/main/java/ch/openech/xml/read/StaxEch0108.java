package ch.openech.xml.read;

import ch.openech.dm.organisation.Organisation;

public class StaxEch0108 {

	public static Organisation process(String xmlString)  {
//		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//		XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		
		return process();
	}
	
	private static Organisation process()  {
		Organisation organisation = null;
//		while (xml.hasNext()) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(ORGANISATION_ROOT)) {
//					organisation = organisationRoot(xml);
//				}
//				else skip(xml);
//			} 
//		}
		return organisation;
	}
	
	private static Organisation organisationRoot()  {
		Organisation organisation = new Organisation(); 

//		while (xml.hasNext()) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(ORGANISATION)) {
//					organisation = organisation(xml, organisation);
//				}
//				else skip(xml);
//			} 
//		}
		return organisation;
	}
	
	public static Organisation organisation(Organisation organisation) {
		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				
//				if (startName.equals(ORGANISATION)) StaxEch0098.organisation(xml, organisation);
//				else if (StringUtils.equals(startName, UIDREG_INFORMATION, COMMERCIAL_REGISTER_INFORMATION, VAT_REGISTER_INFORMATION)) organisationRegistration(xml, organisation);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return organisation;
//			} // else skip
//		}
		return null;
	}
	
	public static void organisationRegistration(Organisation organisation) {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.endsWith("Date")) organisation.set(startName, date(xml));
//				else if (startName.equals(UIDREG_SOURCE)) StaxEch0097.uidStructure(xml, organisation.uidregSourceUid);
//				else organisation.set(startName, token(xml));
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
}
