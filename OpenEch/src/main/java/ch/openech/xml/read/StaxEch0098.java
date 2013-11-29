package ch.openech.xml.read;

import ch.openech.dm.code.TypeOfResidenceOrganisation;
import ch.openech.dm.organisation.Headquarter;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.model.EnumUtils;

public class StaxEch0098 {

	public static Organisation reportedOrganisation()  {
		Organisation organisation = new Organisation();
		return organisation;
		
//		while(true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(ORGANISATION)) organisation(xml, organisation);
//				else if (startName.endsWith("Residence")) residence(startName, xml, organisation);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return organisation;
//			}
//			// else skip
//		}
	}

	public static void residence(String startName, Organisation organisation)  {
		organisation.typeOfResidenceOrganisation = EnumUtils.valueOf(TypeOfResidenceOrganisation.class, startName);
		residence(organisation);
	}
	
	public static void organisation(Organisation organisation)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(ORGANISATION_IDENTIFICATION)) StaxEch0097.organisationIdentification(xml, organisation.identification);
//				else if (StringUtils.equals(startName, FOUNDATION, LIQUIDATION)) foundationOrLiquidation(xml, organisation);
//				else if (StringUtils.equals(startName, UID_BRANCHE_TEXT, NOGA_CODE, LANGUAGE_OF_CORRESPONDANCE)) organisation.set(startName, token(xml));
//				else if (StringUtils.equals(startName, CONTACT)) organisation.contact = StaxEch0046.contact(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
	public static void foundationOrLiquidation(Organisation organisation)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (StringUtils.equals(startName,  FOUNDATION_REASON,  LIQUIDATION_REASON)) organisation.set(startName, token(xml));
//				else if (StringUtils.equals(startName, FOUNDATION_DATE,  LIQUIDATION_DATE, LIQUIDATION_ENTRY_DATE)) organisation.set(startName, StaxEch0044.datePartiallyKnown(xml));
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
	
	// Fast wie in eCH 11
	// -> Eine Verschachtelung weniger (daher nur die residence - methode)
	// -> businessAddress statt dwellingAddress
	// -> federalRegister gibt es hier nicht
	
	public static void residence(Organisation organisation)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (StringUtils.equals(startName, REPORTING_MUNICIPALITY)) organisation.reportingMunicipality = StaxEch0007.municipality(xml);
//				else if (StringUtils.equals(startName, SWISS_HEADQUARTER, FOREIGN_HEADQUARTER)) organisation.headquarter = headquarter(xml);
//				else if (startName.equals(ARRIVAL_DATE)) organisation.arrivalDate = StaxEch.date(xml);
//				else if (startName.equals(COMES_FROM)) organisation.comesFrom = StaxEch0011.destination(xml);
//				else if (startName.equals(BUSINESS_ADDRESS)) organisation.businessAddress = StaxEch0011.dwellingAddress(xml);
//				else if (startName.equals(DEPARTURE_DATE)) organisation.departureDate = StaxEch.date(xml);
//				else if (startName.equals(GOES_TO)) organisation.goesTo = StaxEch0011.destination(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}

	private static Headquarter headquarter()  {
		Headquarter headquarter = new Headquarter();
		return headquarter;
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (StringUtils.equals(startName, ORGANISATION_IDENTIFICATION)) headquarter.identification = StaxEch0097.organisationIdentification(xml);
//				else if (StringUtils.equals(startName, HEADQUARTER_MUNICIPALITY)) headquarter.reportingMunicipality = StaxEch0007.municipality(xml);
//				else if (startName.equals(BUSINESS_ADDRESS)) headquarter.businessAddress = StaxEch0011.dwellingAddress(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) return headquarter;
//			// else skip
//		}
	}
	
}
