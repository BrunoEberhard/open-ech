package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.CORRECT_REPORTING;

import java.io.InputStream;
import java.util.List;

import ch.openech.dm.Event;
import ch.openech.dm.code.TypeOfResidenceOrganisation;
import ch.openech.dm.organisation.Organisation;
import ch.openech.dm.organisation.OrganisationIdentification;
import ch.openech.mj.toolkit.ProgressListener;
import ch.openech.mj.util.StringUtils;
import ch.openech.server.EchPersistence;

public class StaxEch0148 implements StaxEchParser {

	private final EchPersistence persistence;
	
	private Event e;
	private String lastInsertedOrganisationId;
	
	public StaxEch0148(EchPersistence persistence) {
		this.persistence = persistence;
	}
	
	public void insertOrganisation(Organisation organisation) {
		organisation.event = e;
		
		if (organisation.getId() == null) {
			organisation.identification.technicalIds.localId.setOpenEch();
		}
		persistence.organisation().insert(organisation);
		lastInsertedOrganisationId = organisation.getId();
	}

	@Override
	public String getLastInsertedId() {
		return lastInsertedOrganisationId;
	}

	@Override
	public void process(String xmlString)  {
//		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//		XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		
		process(xmlString, null);
		//xml.close();
	}

	public void process(InputStream inputStream, String eventString, ProgressListener progressListener) {
//		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
//		
		process(eventString, progressListener);
		//xml.close();
	}

	private void process(String xmlString, ProgressListener progressListener)  {
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
	}
	
	private void delivery(String xmlString,  ProgressListener progressListener)  {
//		while (!isCanceled(progressListener)) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (StringUtils.equals(startName, DELIVERY_HEADER, HEADER)) {
//					skip(xml);
//				} else {
//					e = new Event();
//					e.type = startName;
//					// e.message = xmlString;
//					e.time = new LocalDateTime();
//					
//					if (startName.equals(ORGANISATION_BASE_DELIVERY)) baseDelivery(xml, progressListener);
//					else if (StringUtils.equals(startName, FOUNDATION, MOVE_IN)) eventAdd(xml);
////					else if (StringUtils.equals(startName, NATURALIZE_FOREIGNER, NATURALIZE_SWISS, UNDO_CITIZEN)) eventNaturalize(startName, xml);
//					else simpleOrganisationEvent(startName, xml);
//				}
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
	private boolean isCanceled(ProgressListener progressListener) {
		return progressListener.isCanceled();
	}
	
	private void baseDelivery(ProgressListener progressListener)  {
		int numberOfOrganisations = 0;
		int count = 0;
		
//		while (!isCanceled(progressListener)) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(REPORTED_ORGANISATION)) {
//					Organisation organisation = StaxEch0098.reportedOrganisation(xml);
//					insertOrganisation(organisation);	
//					if (progressListener != null) progressListener.showProgress(count++, 1000);
//				}
//				else if (startName.equals(NUMBER_OF_ORGANISATIONS)) numberOfOrganisations = StaxEch.integer(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}

	
	//

	public void simpleOrganisationEvent(String type, Organisation organisation) {
		try {
			organisation.event = e;
			persistence.organisation().update(organisation);
			persistence.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//

	public Organisation getOrganisation(OrganisationIdentification organisationIdentification) {
		if (organisationIdentification.getId() != null) {
			return persistence.organisationLocalIdIndex().find(organisationIdentification.getId());
		} else {
			List<Organisation> organisations = persistence.organisationIndex().findObjects(organisationIdentification.organisationName);
			if (!organisations.isEmpty()) {
				return organisations.get(0);
			} else {
				return null;
			}
		}
	}
	
	//

	public void eventAdd()  {
		// Foundation or MoveIn
		Organisation organisation = null;
		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(REPORTED_ORGANISATION)) organisation = StaxEch0098.reportedOrganisation(xml);
//				else if (StringUtils.equals(startName, UIDREG_INFORMATION, COMMERCIAL_REGISTER_INFORMATION, VAT_REGISTER_INFORMATION)) StaxEch0108.organisationRegistration(xml, organisation);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				insertOrganisation(organisation);
//				return;
//			} // else skip
//		}
	}

	private void simpleOrganisationEvent(String eventName)  {
		OrganisationIdentification organisationIdentification = new OrganisationIdentification();
		Organisation organisation = null;
		
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (StringUtils.equals(startName, ORGANISATION_IDENTIFICATION)) {
//					StaxEch0097.organisationIdentification(xml, organisationIdentification);
//					organisation = getOrganisation(organisationIdentification);
//					if (StringUtils.equals(eventName, CORRECT_LIQUIDATION)) {
//						organisation.liquidationEntryDate = null;
//						organisation.liquidationDate = null;
//						organisation.liquidationReason = null;
//					}
//				}
//				// TODO valid dateValidFrom
//			
//				else if (startName.endsWith("Date")) organisation.set(startName, date(xml));
//				else if (startName.equals(UIDREG_SOURCE)) StaxEch0097.uidStructure(xml, organisation.uidregSourceUid);
//				else if (startName.equals(CONTACT)) organisation.contact = StaxEch0046.contact(xml);
//				else if (startName.equals(TYP_OF_RESIDENCE)) StaxEch.enuum(xml, organisation, Organisation.ORGANISATION.typeOfResidenceOrganisation);
//				else if (startName.equals(REPORTING_MUNICIPALITY)) organisation.reportingMunicipality = StaxEch0007.municipality(xml);
//				else if (startName.equals(ARRIVAL_DATE)) organisation.arrivalDate = StaxEch.date(xml);
//				else if (startName.equals(COMES_FROM)) organisation.comesFrom = StaxEch0011.destination(xml);
//				else if (startName.equals(BUSINESS_ADDRESS)) organisation.businessAddress = StaxEch0011.dwellingAddress(xml);
//				else if (StringUtils.equals(startName, HAS_MAIN_RESIDENCE, HAS_SECONDARY_RESIDENCE, HAS_OTHER_RESIDENCE, MOVE_OUT_REPORTING_DESTINATION, MOVE_REPORTING_ADDRESS)) {
//					setTypeOfResidenceInCorrectReportingEvent(eventName, startName, organisation);
//					StaxEch0098.residence(xml, organisation);
//				}
//				else if (StringUtils.equals(startName, FOUNDATION, LIQUIDATION)) StaxEch0098.foundationOrLiquidation(xml, organisation);
//				else organisation.set(startName, token(xml));
//			} else if (event.isEndElement()) {
//				simpleOrganisationEvent(eventName, organisation);
//				return;
//			} // else skip
//		}
	}

	private void setTypeOfResidenceInCorrectReportingEvent(String eventName, String startName, Organisation organisation) {
		// Bei dem Event CorrectReporting wird der TYP_OF_RESIDENCE nicht übertragen. Dennoch ist aufgrund
		// des vorhandenen XML Elements klar, ob ein Wechsel des Typs stattfinden soll. Bei ChangeReporting muss
		// das nicht gemacht werden, weil dort der Typ übertragen wird.
		if (StringUtils.equals(eventName, CORRECT_REPORTING)) {
			organisation.typeOfResidenceOrganisation = (TypeOfResidenceOrganisation) StaxEch.enuum(TypeOfResidenceOrganisation.class, startName);
		}
	}

}
