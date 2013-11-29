package ch.openech.xml.read;

import ch.openech.dm.common.NamedId;
import ch.openech.dm.organisation.OrganisationIdentification;
import ch.openech.dm.organisation.UidStructure;

public class StaxEch0097 {

	public static OrganisationIdentification organisationIdentification()  {
		OrganisationIdentification organisationIdentification = new OrganisationIdentification();
		organisationIdentification(organisationIdentification);
		return organisationIdentification;
	}

	public static void organisationIdentification(OrganisationIdentification organisation)  {
//		 
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				
//				if (startName.equals(UID)) uidStructure(xml, organisation.uid);
//				
//				else if (startName.equals(LOCAL_ORGANISATION_ID)) namedOrganisationId(xml, organisation.technicalIds.localId);
//				else if (startName.equals("otherOrganisationId") || startName.equals(_OTHER_ORGANISATION_ID)) organisation.technicalIds.otherId.add(namedOrganisationId(xml));
//				
//				else if (StringUtils.equals(startName, ORGANISATION_NAME, ORGANISATION_LEGAL_NAME, ORGANISATION_ADDITIONAL_NAME, LEGAL_FORM)) organisation.set(startName, token(xml));
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
	public static NamedId namedOrganisationId()  {
		NamedId namedId = new NamedId();
		namedOrganisationId(namedId);
		return namedId;
	}
	
	public static void namedOrganisationId(NamedId namedId)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (StringUtils.equals(startName, ORGANISATION_ID_CATEGORY)) namedId.personIdCategory = token(xml);
//				else if (startElement.getName().getLocalPart().equals(ORGANISATION_ID)) namedId.personId = token(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
	public static void uidStructure(UidStructure uid)  {
		String uidOrganisationIdCategorie = null;
		String uidOrganisationId = null;
		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (StringUtils.equals(startName, UID_ORGANISATION_ID_CATEGORIE, "uidOrganisationIdCategory")) uidOrganisationIdCategorie = token(xml);
//				else if (startElement.getName().getLocalPart().equals(UID_ORGANISATION_ID)) uidOrganisationId = token(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				if (!StringUtils.isBlank(uidOrganisationIdCategorie) && !StringUtils.isBlank(uidOrganisationId)) {
//					uid.value = uidOrganisationIdCategorie + uidOrganisationId;
//				} 
//				return;
//			} // else skip
//		}
	}
	
}
