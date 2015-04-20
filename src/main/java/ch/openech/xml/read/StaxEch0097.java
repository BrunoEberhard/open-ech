package ch.openech.xml.read;

import static ch.openech.model.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.model.ViewUtil;
import org.minimalj.util.StringUtils;

import ch.openech.model.common.NamedId;
import ch.openech.model.organisation.Organisation;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.model.organisation.UidStructure;
import ch.openech.model.organisation.types.LegalForm;

public class StaxEch0097 {

	public static OrganisationIdentification organisationIdentification(XMLEventReader xml) throws XMLStreamException {
		Organisation organisation = new Organisation();
		organisationIdentification(xml, organisation);
		return ViewUtil.view(organisation, new OrganisationIdentification());
	}
	
	public static NamedId namedOrganisationId(XMLEventReader xml) throws XMLStreamException {
		NamedId namedId = new NamedId();
		namedOrganisationId(xml, namedId);
		return namedId;
	}
	
	public static void namedOrganisationId(XMLEventReader xml, NamedId namedId) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (StringUtils.equals(startName, ORGANISATION_ID_CATEGORY)) namedId.personIdCategory = token(xml);
				else if (startElement.getName().getLocalPart().equals(ORGANISATION_ID)) namedId.personId = token(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	public static void uidStructure(XMLEventReader xml, UidStructure uid) throws XMLStreamException {
		String uidOrganisationIdCategorie = null;
		String uidOrganisationId = null;
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (StringUtils.equals(startName, UID_ORGANISATION_ID_CATEGORIE, "uidOrganisationIdCategory")) uidOrganisationIdCategorie = token(xml);
				else if (startElement.getName().getLocalPart().equals(UID_ORGANISATION_ID)) uidOrganisationId = token(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				if (!StringUtils.isBlank(uidOrganisationIdCategorie) && !StringUtils.isBlank(uidOrganisationId)) {
					uid.value = uidOrganisationIdCategorie + uidOrganisationId;
				} 
				return;
			} // else skip
		}
	}

	public static void organisationIdentification(XMLEventReader xml, Organisation organisation) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				
				if (startName.equals(UID)) uidStructure(xml, organisation.uid);
				
				else if (startName.equals(LOCAL_ORGANISATION_ID)) namedOrganisationId(xml, organisation.technicalIds.localId);
				else if (startName.equals("otherOrganisationId") || startName.equals(_OTHER_ORGANISATION_ID)) organisation.technicalIds.otherId.add(namedOrganisationId(xml));
				
				else if (StringUtils.equals(startName, ORGANISATION_NAME, ORGANISATION_LEGAL_NAME, ORGANISATION_ADDITIONAL_NAME)) organisation.set(startName, token(xml));
				else if (StringUtils.equals(startName, LEGAL_FORM)) organisation.legalForm = new LegalForm(token(xml));
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
}