package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.FOUNDATION;
import static ch.openech.dm.XmlConstants.INLIQUIDATION;
import static ch.openech.dm.XmlConstants.KEY_EXCHANGE;
import static ch.openech.dm.XmlConstants.LIQUIDATION;
import static ch.openech.dm.XmlConstants.LIQUIDATION_DATE;
import static ch.openech.dm.XmlConstants.LIQUIDATION_ENTRY_DATE;
import static ch.openech.dm.XmlConstants.LIQUIDATION_REASON;
import static ch.openech.dm.XmlConstants.MOVE_IN;
import static ch.openech.dm.XmlConstants.NUMBER_OF_ORGANISATIONS;
import static ch.openech.dm.XmlConstants.ORGANISATION_BASE_DELIVERY;
import static ch.openech.dm.XmlConstants.REPORTED_ORGANISATION;

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
	
	public String liquidation(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(LIQUIDATION, organisation);
        event.values(organisation, LIQUIDATION_DATE, LIQUIDATION_REASON);
        return result();
	}

	public String inliquidation(Organisation organisation) throws Exception {
		WriterElement event = simpleOrganisationEvent(INLIQUIDATION, organisation);
        event.values(organisation, LIQUIDATION_ENTRY_DATE, LIQUIDATION_REASON);
        // TODO contact
        return result();
	}

	public String moveIn(Organisation organisation) throws Exception {
		WriterElement foundation = delivery().create(URI, MOVE_IN);
		ech98.reportedOrganisationType(foundation, REPORTED_ORGANISATION, organisation);
        return result();
	}
	
}
