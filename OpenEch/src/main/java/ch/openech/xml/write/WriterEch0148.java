package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.*;

import java.util.List;

import ch.openech.dm.organisation.Organisation;

public class WriterEch0148 extends DeliveryWriter {

	public final String URI;
	
	public final WriterEch0007 ech7;
	public final WriterEch0046 ech46;
	public final WriterEch0097 ech97;
	public final WriterEch0098 ech98;
	// public final WriterEch0102 ech102;
	public final WriterEch0108 ech108;
	
	public WriterEch0148(EchNamespaceContext context) {
		super(context);
		
		URI = context.getNamespaceURI(148);
		ech7 = new WriterEch0007(context);
		ech46 = new WriterEch0046(context);
		ech97 = new WriterEch0097(context);
		ech98 = new WriterEch0098(context);
//		ech102 = new WriterEch0102(context);
		ech108 = new WriterEch0108(context);
	}
	
	@Override
	protected int getSchemaNumber() {
		return 148;
	}
	
	@Override
	protected String getNamespaceURI() {
		return URI;
	}
	
	// 
	
	public String organisationBaseDelivery(List<Organisation> organisations) throws Exception {
		WriterElement organisationBaseDelivery = delivery().create(URI, ORGANISATION_BASE_DELIVERY);
		for (Organisation organisation : organisations) {
			ech98.reportedOrganisationType(organisationBaseDelivery, REPORTED_ORGANISATION, organisation);
		}
		organisationBaseDelivery.text(NUMBER_OF_ORGANISATIONS, String.valueOf(organisations.size()));
		return result();
	}

	public String keyExchange(List<Organisation> organisations) throws Exception {
		WriterElement organisationBaseDelivery = delivery().create(URI, KEY_EXCHANGE);
		for (Organisation organisation : organisations) {
			ech97.organisationIdentification(organisationBaseDelivery, organisation);
		}
		return result();
	}

	// Elemente für alle Events
	
	private WriterElement simpleOrganisationEvent(String eventName, Organisation organisation) throws Exception {
		return simpleOrganisationEvent(delivery(), eventName, organisation);
	}
	
	private WriterElement simpleOrganisationEvent(WriterElement delivery, String eventName, Organisation organisation) throws Exception {
		WriterElement event = delivery.create(URI, eventName);
		ech97.organisationIdentification(event, organisation);
        return event;
	}
	
	//
	
	public String foundation(Organisation organisation) throws Exception {
		WriterElement foundation = delivery().create(URI, FOUNDATION);
		ech98.reportedOrganisationType(foundation, REPORTED_ORGANISATION, organisation);
		ech108.uidregInformation(foundation, organisation);
		ech108.commercialRegisterInformation(foundation, organisation);
		ech108.vatRegisterInformation(foundation, organisation);
        return result();
	}

	// Code 4
	public String moveIn(Organisation organisation) throws Exception {
		WriterElement moveIn = delivery().create(URI, MOVE_IN);
		ech98.reportedOrganisationType(moveIn, REPORTED_ORGANISATION, organisation);
        return result();
	}
	
	// Code 5
	public String moveOut(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(MOVE_OUT, organisation);
		moveOutReportingDestination(event, organisation);
		// TODO moveDate
		return result();
	}
	
	private void moveOutReportingDestination(WriterElement parent, Organisation organisation) throws Exception {
		WriterElement element = parent.create(URI, MOVE_OUT_REPORTING_DESTINATION);
		ech7.municipality(element, REPORTING_MUNICIPALITY, organisation.reportingMunicipality);
		element.values(organisation, DEPARTURE_DATE);
		ech98.destination(element, GOES_TO, organisation.goesTo, true);
	}
	
	// Code 6
	public String move(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(MOVE, organisation);
		moveReportingAddress(event, organisation);
		// TODO moveDate
		return result();
	}
	
	private void moveReportingAddress(WriterElement parent, Organisation organisation) throws Exception {
		WriterElement element = parent.create(URI, MOVE_REPORTING_ADDRESS);
		ech7.municipality(element, REPORTING_MUNICIPALITY, organisation.reportingMunicipality);
		ech98.dwellingAddress(element, BUSINESS_ADDRESS, organisation.businessAddress);
	}
	
	// Code 7
	public String contact(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CONTACT, organisation);
		ech46.contact(event, organisation.contact);
		// TODO validFrom, validTill
		return result();
	}
	
	// Code 8
	public String changeOrganisationName(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CHANGE_ORGANISATION_NAME, organisation);
		event.values(organisation, ORGANISATION_NAME, ORGANISATION_LEGAL_NAME, ORGANISATION_ADDITIONAL_NAME);
		// TODO validFrom
		return result();
	}

	// Code 9
	public String changeLegalForm(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CHANGE_LEGAL_FORM, organisation);
        event.values(organisation, LEGAL_FORM);
        // TODO validFrom
        return result();
	}

	// Code 10
	public String changeReporting(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CHANGE_REPORTING, organisation);
		ech7.municipality(event, REPORTING_MUNICIPALITY, organisation.reportingMunicipality);
		// event.values(organisation, TYP_OF_RESIDENCE, ARRIVAL_DATE);
		event.text(TYP_OF_RESIDENCE, organisation.typeOfResidenceOrganisation);
		event.values(organisation, ARRIVAL_DATE);
		ech98.destination(event, COMES_FROM, organisation.comesFrom, false);
		ech98.dwellingAddress(event, BUSINESS_ADDRESS, organisation.businessAddress);
		return result();
	}
	
	// Code 11
	public String inLiquidation(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(INLIQUIDATION, organisation);
		event.values(organisation, LIQUIDATION_ENTRY_DATE, LIQUIDATION_REASON);
		ech46.contact(event, organisation.contact);
		return result();
	}
	
	// Code 12
	public String liquidation(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(LIQUIDATION, organisation);
		event.values(organisation, LIQUIDATION_DATE, LIQUIDATION_REASON);
		ech46.contact(event, organisation.contact);
		return result();
	}
	
	// Code 13
	public String correctOrganisationName(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CORRECT_ORGANISATION_NAME, organisation);
		event.values(organisation, ORGANISATION_NAME, ORGANISATION_LEGAL_NAME, ORGANISATION_ADDITIONAL_NAME);
		return result();
	}
	
	// Code 14
	public String correctLegalForm(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CORRECT_LEGAL_FORM, organisation);
		event.values(organisation, LEGAL_FORM);
		return result();
	}
	
	// Code 15
	public String correctUidBranche(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CORRECT_UID_BRANCHE, organisation);
		event.values(organisation, UID_BRANCHE_TEXT, NOGA_CODE);
		return result();
	}

	// Code 16 scheint es nicht zugeben

	// Code 17
	public String correctFoundation(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CORRECT_FOUNDATION, organisation);
        ech98.foundation(event, organisation);
		return result();
	}
	
	// Code 18
	public String correctLiquidation(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CORRECT_LIQUIDATION, organisation);
        event.values(organisation, LIQUIDATION_ENTRY_DATE);
        ech98.liquidation(event, organisation);
		ech46.contact(event, organisation.contact);
		return result();
	}
	
	// Code 19
	public String correctContact(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CORRECT_CONTACT, organisation);
		ech46.contact(event, organisation.contact);
		return result();
	}

	// Code 20
	public String correctLanguageOfCorrespondance(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CORRECT_LANGUAGE_OF_CORRESPONDANCE, organisation);
        event.values(organisation, LANGUAGE_OF_CORRESPONDANCE);
        // TODO validFrom
        return result();
	}
	
	// Code 21
	public String correctReporting(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(CORRECT_REPORTING, organisation);
		ech98.residence(event, organisation);
        return result();
	}

}
