package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import java.io.InputStream;
import java.io.StringReader;
import java.sql.SQLException;

import javax.swing.ProgressMonitor;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.Event;
import ch.openech.dm.XmlConstants;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Relation;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.ProgressListener;
import ch.openech.mj.util.StringUtils;
import ch.openech.server.EchPersistence;

public class StaxEch0148 {

	private final EchPersistence persistence;
	
	private Organisation organisationToChange = null;
	private Event e;
	private String lastInsertedOrganisationId;
	
	public StaxEch0148(EchPersistence persistence) {
		this.persistence = persistence;
	}
	
//	public void insertOrganisation(Organisation organisation) {
//		try {
//			organisation.event = e;
//			persistence.organisation().insert(organisation);
//			lastInsertedOrganisationId = organisation.getId();
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	public String getLastInsertedOrganisationId() {
//		return lastInsertedOrganisationId;
//	}
//
//	public void simpleOrganisationEvent(String type, Organisation organisationIdentification, Organisation organisation) {
//		if (StringUtils.equals(type, XmlConstants.DIVORCE, XmlConstants.UNDO_MARRIAGE, XmlConstants.UNDO_PARTNERSHIP)) removePartner(organisation);
//
//		try {
//			organisation.event = e;
//			persistence.organisation().update(organisation);
//			persistence.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	//
//
//	public Organisation getOrganisation(Organisation organisation) {
//		if (organisation.getId() != null) {
//			return persistence.organisation().getByLocalOrganisationId(organisation.getId());
//		} else if (!StringUtils.isBlank(organisation.vn)) {
//			return persistence.organisation().getByVn(organisation.vn);
//		} else {
//			return persistence.organisation().getByName(organisation.officialName, organisation.firstName, organisation.dateOfBirth);
//		}
//	}
//	
//	//
//	
//	public void process(String xmlString) throws XMLStreamException {
//		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//		XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
//		
//		process(xml, xmlString, null);
//		xml.close();
//	}
//
//	public void process(InputStream inputStream, String eventString, ProgressListener progressListener) throws XMLStreamException {
//		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
//		
//		process(xml, eventString, progressListener);
//		xml.close();
//	}
//
//	private void process(XMLEventReader xml, String xmlString, ProgressListener progressListener) throws XMLStreamException {
//		while (xml.hasNext() && !isCanceled(progressListener)) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(DELIVERY)) {
//					delivery(xmlString, xml, progressListener);
//				}
//				else skip(xml);
//			} 
//		}
//	}
//	
//	private void delivery(String xmlString, XMLEventReader xml, ProgressListener progressListener) throws XMLStreamException {
//		while (!isCanceled(progressListener)) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(DELIVERY_HEADER)) {
//					skip(xml);
//				} else {
//					e = new Event();
//					e.type = startName;
//					// e.message = xmlString;
//					e.time = DateUtils.getToday();
//					
//					if (startName.equals(ORGANISATION_BASE_DELIVERY)) baseDelivery(xml, progressListener);
//					else if (startName.equals(BIRTH)) eventBirth(xml);
//					else if (startName.equals(MOVE_IN)) eventMoveIn(xml);
//					else if (StringUtils.equals(startName, NATURALIZE_FOREIGNER, NATURALIZE_SWISS, UNDO_CITIZEN)) eventNaturalize(startName, xml);
//					else simpleOrganisationEvent(startName, xml);
//				}
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
//	}
//	
//	private boolean isCanceled(ProgressListener progressListener) {
//		return (progressListener instanceof ProgressMonitor) && ((ProgressMonitor)progressListener).isCanceled();
//	}
//	
//	private void baseDelivery(XMLEventReader xml, ProgressListener progressListener) throws XMLStreamException {
//		int numberOfOrganisations = 0;
//		int count = 0;
//		
//		while (!isCanceled(progressListener)) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(REPORTED_ORGANISATION)) {
//					reportedOrganisation(xml);
//					if (progressListener != null) progressListener.showProgress(count++, 1000);
//				}
//				else if (startName.equals(NUMBER_OF_ORGANISATIONS)) numberOfOrganisations = StaxEch.integer(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
//	}
//
//	public void reportedOrganisation(XMLEventReader xml) throws XMLStreamException {
//		Organisation organisation = new Organisation();
//		
//		while(true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(ORGANISATION)) StaxEch0098.organisation(xml, organisation);
//				else residenceChoiceOrSkip(startName, xml, organisation);
//			} else if (event.isEndElement()) {
//				insertOrganisation(organisation);	
//				return;
//			}
//			// else skip
//		}
//	}
//	
//	public static void residenceChoiceOrSkip(String startName, XMLEventReader xml, Organisation organisation) throws XMLStreamException {
//		if (startName.equals(HAS_MAIN_RESIDENCE)) {
//			organisation.typeOfResidenceOrganisation = "1";
//			StaxEch0011.mainResidenceType(xml, organisation);
//		} else if (startName.equals(HAS_SECONDARY_RESIDENCE)) {
//			organisation.typeOfResidenceOrganisation = "2";
//			StaxEch0011.secondaryResidenceType(xml, organisation);
//		} else if (startName.equals(HAS_OTHER_RESIDENCE)) {
//			organisation.typeOfResidenceOrganisation = "3";
//			StaxEch0011.otherResidenceType(xml, organisation);
//		} else {
//			System.out.println("Skipping: " + startName);
//			skip(xml);
//		}
//	}
//
//	public void extension(XMLEventReader xml, Organisation organisationToChange) throws XMLStreamException {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (CONTACT.equals(startName)) organisationToChange.contact = StaxEch0046.contact(xml);
//				else if ("organisationExtendedInformation".equals(startName)) organisationToChange.organisationExtendedInformation = StaxEch0101.information(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
//	}
//	
//	public void baseDeliveryOrganisation(XMLEventReader xml, Organisation values) throws XMLStreamException {
//		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(PERSON)) StaxEch0011.organisation(xml, values);
//				else if (startName.equals(ANY_PERSON)) StaxEch0011.anyOrganisation(xml, values);
//				else if (startName.equals(PLACE_OF_ORIGIN_ADDON)) StaxEch0021.addPlaceOfOriginAddon(xml, values.placeOfOrigin);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
//	}
//
//	public void eventBirth(XMLEventReader xml) throws XMLStreamException {
//		Organisation organisation = null;
//		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(BIRTH_PERSON)) organisation = newOrganisation(xml);
//				else if (startName.equals(MOTHER)) birthParent(false, xml, organisation);
//				else if (startName.equals(FATHER)) birthParent(true, xml, organisation);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				completePlaceOfOrigins(organisation);
//				copyResidence(organisation);
//				insertOrganisation(organisation);
//				return;
//			} // else skip
//		}
//	}
//	
//	private void completePlaceOfOrigins(Organisation organisation) {
//		for (PlaceOfOrigin placeOfOrigin : organisation.placeOfOrigin) {
//			placeOfOrigin.naturalizationDate = organisation.getDateOfBirth();
//			placeOfOrigin.reasonOfAcquisition = "1"; // Abstammung
//		}
//	}
//	
//	private void copyResidence(Organisation organisation) {
//		Relation relation = organisation.getMother();
//		if (relation.isEmpty()) relation = organisation.getFather();
//		if (relation.isEmpty()) {
//			// TODO was macht man hier? Einfach so entstehen ungültige Daten, da eine Organisation einen Meldeort haben muss
//			return;
//		}
//		Organisation parent = getOrganisation(relation.partner);
//		if (parent == null) return;
//		organisation.typeOfResidence = parent.typeOfResidence;
//		organisation.residence.reportingMunicipality = parent.residence.reportingMunicipality;
//		organisation.residence.setSecondary(parent.residence.secondary);
//	}
//
//	public void eventMoveIn(XMLEventReader xml) throws XMLStreamException {
//		Organisation organisation = null;
//		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(MOVE_IN_PERSON)) organisation = newOrganisation(xml);
//				else if (startName.equals(NAME_OF_FATHER)) StaxEch0021.nameOfParentAtBirth(xml, organisation.getFather());
//				else if (startName.equals(NAME_OF_MOTHER)) StaxEch0021.nameOfParentAtBirth(xml, organisation.getMother());
//				else if (startName.equals(OCCUPATION)) organisation.occupation.add(StaxEch0021.occupation(xml));
//				else if (startName.equals(_RELATIONSHIP) || startName.equals(RELATIONSHIP)) StaxEch0021.relation(xml, organisation);
//				else if (startName.equals(EXTENSION)) extension(xml, organisation);
//				else residenceChoiceOrSkip(startName, xml, organisation);
//			} else if (event.isEndElement()) {
//				insertOrganisation(organisation);
//				return;
//			} // else skip
//		}
//	}
//	
//	public void moveOutReportingDestination(XMLEventReader xml, Organisation organisationToChange) throws XMLStreamException {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(REPORTING_MUNICIPALITY)) organisationToChange.residence.reportingMunicipality = StaxEch0007.municipality(xml);
//				else if (startName.equals(FEDERAL_REGISTER)) organisationToChange.residence.reportingMunicipality = federalRegister(xml);
//				else if (startName.equals(GOES_TO)) organisationToChange.goesTo = StaxEch0011.destination(xml);
//				else if (startName.equals(DEPARTURE_DATE)) organisationToChange.departureDate = StaxEch.date(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
//	}
//
//	
//	public void moveReportingAddress(XMLEventReader xml, Organisation organisationToChange) throws XMLStreamException {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(REPORTING_MUNICIPALITY)) organisationToChange.residence.reportingMunicipality = StaxEch0007.municipality(xml);
//				else if (startName.equals(FEDERAL_REGISTER)) organisationToChange.residence.reportingMunicipality = federalRegister(xml);
//				else if (startName.equals(DWELLING_ADDRESS)) organisationToChange.dwellingAddress = StaxEch0011.dwellingAddress(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
//	}
//	
//	
//	public void changeResidenceTypeReportingMunicipality(XMLEventReader xml, Organisation organisationToChange) throws XMLStreamException {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(REPORTING_MUNICIPALITY)) organisationToChange.residence.reportingMunicipality = StaxEch0007.municipality(xml);
//				else if (startName.equals(TYP_OF_RESIDENCE)) organisationToChange.typeOfResidence = token(xml);
//				else if (startName.equals(ARRIVAL_DATE)) organisationToChange.arrivalDate = StaxEch.date(xml);
//				else if (startName.equals(COMES_FROM)) organisationToChange.comesFrom = StaxEch0011.destination(xml);
//				else if (startName.equals(DWELLING_ADDRESS)) organisationToChange.dwellingAddress = StaxEch0011.dwellingAddress(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
//	}
//	
//	// correctOccupation etc
//	private void simpleOrganisationEvent(String eventName, XMLEventReader xml) throws XMLStreamException {
//		Organisation organisationIdentification = new Organisation();
//		Organisation organisation = null;
//		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (StringUtils.equals(startName, ORGANISATION_IDENTIFICATION)) {
//					StaxEch0097.organisationIdentification(xml, organisationIdentification);
//					organisation = getOrganisation(organisationIdentification);
//				}
//
//				else if (startName.equals(LIQUIDATION_DATE)) organisation.liquidationDate = StaxEch.date(xml);
//				else if (startName.equals(LIQUIDATION_REASON)) organisation.liquidationReason = token(xml);
//				// TODO Was ist mit LIQUIDATION_ENTRY_DATE??
//				else if (startName.equals(LIQUIDATION_ENTRY_DATE)) organisation.liquidationDate = token(xml);
//				// TODO contact
//				
//				else if (startName.equals(ORGANISATION_NAME)) organisation.organisationName = StaxEch.date(xml);
//				else if (startName.equals(ORGANISATION_LEGAL_NAME)) organisation.organisationLegalName = StaxEch.date(xml);
//				else if (startName.equals(ORGANISATION_ADDITIONAL_NAME)) organisation.organisationAdditionalName = StaxEch.date(xml);
//				// TODO valid dateValidFrom
//				
//				else if (startName.equals(LEGAL_FORM)) organisation.legalForm = StaxEch.date(xml);
//			
//				else if (startName.equals(CHANGE_REPORTING)) changeResidenceTypeReportingMunicipality(xml, organisationToChange);
//				else if (startName.equals(EXTENSION)) extension(xml, organisationToChange);
//				else residenceChoiceOrSkip(startName, xml, organisationToChange);
//			} else if (event.isEndElement()) {
//				simpleOrganisationEvent(eventName, organisationIdentification, organisation);
//				return;
//			} // else skip
//		}
//	}
//	

}
