package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.ARRIVAL_DATE;
import static ch.openech.dm.XmlConstants.BUSINESS_ADDRESS;
import static ch.openech.dm.XmlConstants.COMES_FROM;
import static ch.openech.dm.XmlConstants.CONTACT;
import static ch.openech.dm.XmlConstants.DEPARTURE_DATE;
import static ch.openech.dm.XmlConstants.FOREIGN_HEADQUARTER;
import static ch.openech.dm.XmlConstants.FOUNDATION;
import static ch.openech.dm.XmlConstants.FOUNDATION_DATE;
import static ch.openech.dm.XmlConstants.FOUNDATION_REASON;
import static ch.openech.dm.XmlConstants.GOES_TO;
import static ch.openech.dm.XmlConstants.HEADQUARTER_MUNICIPALITY;
import static ch.openech.dm.XmlConstants.LANGUAGE_OF_CORRESPONDANCE;
import static ch.openech.dm.XmlConstants.LIQUIDATION;
import static ch.openech.dm.XmlConstants.LIQUIDATION_DATE;
import static ch.openech.dm.XmlConstants.LIQUIDATION_ENTRY_DATE;
import static ch.openech.dm.XmlConstants.LIQUIDATION_REASON;
import static ch.openech.dm.XmlConstants.NOGA_CODE;
import static ch.openech.dm.XmlConstants.ORGANISATION;
import static ch.openech.dm.XmlConstants.ORGANISATION_IDENTIFICATION;
import static ch.openech.dm.XmlConstants.REPORTING_MUNICIPALITY;
import static ch.openech.dm.XmlConstants.SWISS_HEADQUARTER;
import static ch.openech.dm.XmlConstants.UID_BRANCHE_TEXT;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.code.TypeOfResidenceOrganisation;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.db.model.EnumUtils;
import ch.openech.mj.util.StringUtils;

public class StaxEch0098 {

	public static Organisation reportedOrganisation(XMLEventReader xml) throws XMLStreamException {
		Organisation organisation = new Organisation();
		
		while(true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(ORGANISATION)) organisation(xml, organisation);
				else if (startName.endsWith("Residence")) residence(startName, xml, organisation);
				else skip(xml);
			} else if (event.isEndElement()) {
				return organisation;
			}
			// else skip
		}
	}

	public static void residence(String startName, XMLEventReader xml, Organisation organisation) throws XMLStreamException {
		organisation.typeOfResidenceOrganisation = EnumUtils.valueOf(TypeOfResidenceOrganisation.class, startName);
		residence(xml, organisation);
	}
	
	public static void organisation(XMLEventReader xml, Organisation organisation) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(ORGANISATION_IDENTIFICATION)) StaxEch0097.organisationIdentification(xml, organisation);
				else if (StringUtils.equals(startName, FOUNDATION, LIQUIDATION)) foundationOrLiquidation(xml, organisation);
				else if (StringUtils.equals(startName, UID_BRANCHE_TEXT, NOGA_CODE, LANGUAGE_OF_CORRESPONDANCE)) organisation.set(startName, token(xml));
				else if (StringUtils.equals(startName, CONTACT)) organisation.contact = StaxEch0046.contact(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	public static void foundationOrLiquidation(XMLEventReader xml, Organisation organisation) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (StringUtils.equals(startName,  FOUNDATION_REASON,  LIQUIDATION_REASON)) organisation.set(startName, token(xml));
				else if (StringUtils.equals(startName, FOUNDATION_DATE,  LIQUIDATION_DATE, LIQUIDATION_ENTRY_DATE)) organisation.set(startName, StaxEch0044.datePartiallyKnown(xml));
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	
	// Fast wie in eCH 11
	// -> Eine Verschachtelung weniger (daher nur die residence - methode)
	// -> businessAddress statt dwellingAddress
	// -> federalRegister gibt es hier nicht
	
	public static void residence(XMLEventReader xml, Organisation organisation) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (StringUtils.equals(startName, REPORTING_MUNICIPALITY)) organisation.reportingMunicipality = StaxEch0007.municipality(xml);
				else if (StringUtils.equals(startName, SWISS_HEADQUARTER, FOREIGN_HEADQUARTER)) organisation.headquarterOrganisation = headquarter(xml);
				else if (startName.equals(ARRIVAL_DATE)) organisation.arrivalDate = StaxEch.date(xml);
				else if (startName.equals(COMES_FROM)) organisation.comesFrom = StaxEch0011.destination(xml);
				else if (startName.equals(BUSINESS_ADDRESS)) organisation.businessAddress = StaxEch0011.dwellingAddress(xml);
				else if (startName.equals(DEPARTURE_DATE)) organisation.departureDate = StaxEch.date(xml);
				else if (startName.equals(GOES_TO)) organisation.goesTo = StaxEch0011.destination(xml);
				else skip(xml);
			} else if (event.isEndElement()) return;
			// else skip
		}
	}

	private static Organisation headquarter(XMLEventReader xml) throws XMLStreamException {
		Organisation organisation = new Organisation();
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (StringUtils.equals(startName, ORGANISATION_IDENTIFICATION)) StaxEch0097.organisationIdentification(xml, organisation);
				else if (StringUtils.equals(startName, HEADQUARTER_MUNICIPALITY)) organisation.reportingMunicipality = StaxEch0007.municipality(xml);
				else if (startName.equals(BUSINESS_ADDRESS)) organisation.businessAddress = StaxEch0011.dwellingAddress(xml);
				else skip(xml);
			} else if (event.isEndElement()) return organisation;
			// else skip
		}
	}
	
}
