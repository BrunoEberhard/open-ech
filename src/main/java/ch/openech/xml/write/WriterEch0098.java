package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.DwellingAddress;
import ch.openech.model.common.Place;
import ch.openech.model.organisation.Organisation;

public class WriterEch0098 {

	public final String URI;
	public final WriterEch0007 ech7;
	public final WriterEch0008 ech8;
	public final WriterEch0010 ech10;
	public final WriterEch0046 ech46;
	public final WriterEch0097 ech97;
	
	public WriterEch0098(EchSchema context) {
		URI = context.getNamespaceURI(98);
		ech7 = new WriterEch0007(context);
		ech8 = new WriterEch0008(context);
		ech10 = new WriterEch0010(context);
		ech46 = new WriterEch0046(context);
		ech97 = new WriterEch0097(context);
	}

	public void reportedOrganisationType(WriterElement parent, String tagName, Organisation organisation) throws Exception {
		WriterElement element = parent.create(URI, tagName);
		organisation(element, ORGANISATION, organisation);
		residence(element, organisation); 
	}

	public void residence(WriterElement element, Organisation organisation) throws Exception {
		switch (organisation.typeOfResidenceOrganisation) {
		case hasMainResidence: mainResidence(element, organisation); break;
		case hasSecondaryResidence: secondaryResidence(element, organisation); break;
		case hasOtherResidence: otherResidence(element, organisation); break;
		default:
			break;
		}
	}
	
	public void mainResidence(WriterElement parent, Organisation values) throws Exception {
		WriterElement informationElement = parent.create(URI, HAS_MAIN_RESIDENCE);
		ech7.municipality(informationElement, REPORTING_MUNICIPALITY, values.reportingMunicipality);
		commonResidenceInformation(informationElement, values);
	}

	public void secondaryResidence(WriterElement parent, Organisation values) throws Exception {
		WriterElement informationElement = parent.create(URI, HAS_SECONDARY_RESIDENCE);
		swissHeadquarter(informationElement, values);
		ech7.municipality(informationElement, REPORTING_MUNICIPALITY, values.reportingMunicipality);
		commonResidenceInformation(informationElement, values);
	}
	
	private void swissHeadquarter(WriterElement parent, Organisation values) throws Exception {
		if (values.headquarter != null) {
			WriterElement element = parent.create(URI, SWISS_HEADQUARTER);
			ech97.organisationIdentification(element, values.headquarter.identification);
			ech7.municipality(element, HEADQUARTER_MUNICIPALITY, values.headquarter.reportingMunicipality);
			dwellingAddress(element, BUSINESS_ADDRESS, values.headquarter.businessAddress);
		}
	}
	
	public void otherResidence(WriterElement parent, Organisation values) throws Exception {
		WriterElement informationElement = parent.create(URI, HAS_OTHER_RESIDENCE);
		foreignHeadquarter(informationElement, values);
		ech7.municipality(informationElement, REPORTING_MUNICIPALITY, values.reportingMunicipality);
		commonResidenceInformation(informationElement, values);
	}

	private void foreignHeadquarter(WriterElement parent, Organisation values) throws Exception {
		if (values.headquarter != null) {
			WriterElement element = parent.create(URI, FOREIGN_HEADQUARTER);
			dwellingAddress(element, BUSINESS_ADDRESS, values.headquarter.businessAddress);
		}
	}

	// Nearly exact code from Writer Ech0011
	private void commonResidenceInformation(WriterElement element, Organisation values) throws Exception {
		element.values(values, ARRIVAL_DATE);
		destination(element, COMES_FROM, values.comesFrom, true);
		dwellingAddress(element, BUSINESS_ADDRESS, values.businessAddress);
		element.values(values, DEPARTURE_DATE);
		destination(element, GOES_TO, values.goesTo, false);
	}
	
	// Nearly exact code from Writer Ech0011
	public void destination(WriterElement parent, String tagName, Place place, boolean required) throws Exception {
		if (place == null || (!required && place.isUnknown())) return;
		
		WriterElement placeElement = parent.create(URI, tagName);
		if (place.isUnknown()) unknown(placeElement);
		else if (place.isSwiss()) {
			ech7.municipality(placeElement, SWISS_TOWN, place.municipalityIdentification);
		} else if (place.isForeign()) {
			WriterElement foreignCountry = placeElement.create(URI, FOREIGN_COUNTRY);
			ech8.country(foreignCountry, COUNTRY, place.countryIdentification);
			foreignCountry.text(TOWN, place.foreignTown);
		}
		if (place.mailAddress != null && !place.mailAddress.isEmpty()) ech10.addressInformation(placeElement, MAIL_ADDRESS, place.mailAddress);
	}
	
	// Quite different from Writer Ech0011
	public void dwellingAddress(WriterElement parent, String tagName, DwellingAddress dwelingAddress) throws Exception {
		if (dwelingAddress == null) return; 
		
		WriterElement element = parent.create(URI, tagName);
		element.values(dwelingAddress, _E_G_I_D, _E_W_I_D);
		if (dwelingAddress.mailAddress != null && !dwelingAddress.mailAddress.isEmpty()) ech10.swissAddressInformation(element, ADDRESS, dwelingAddress.mailAddress);
		element.values(dwelingAddress, MOVING_DATE);
	}
	
	private static void unknown(WriterElement parent) throws Exception {
		parent.text(UNKNOWN, "0");
	}
	
	//
	
	public void organisation(WriterElement parent, Organisation values) throws Exception {
		organisation(parent, ORGANISATION, values);
	}
	
	public void organisation(WriterElement parent, String tagName, Organisation values) throws Exception {
		WriterElement element = parent.create(URI, tagName);

		ech97.organisationIdentification(element, values);
		element.values(values, UID_BRANCHE_TEXT, NOGA_CODE);
		foundation(element, values);
		liquidation(element, values);
		ech46.contact(element, values.contacts);
		element.values(values, LANGUAGE_OF_CORRESPONDANCE);
	}
	
	public void foundation(WriterElement parent, Organisation values) throws Exception {
		WriterElement element = parent.create(URI, FOUNDATION);
		datePartiallyKnownType(element, FOUNDATION_DATE, values.foundationDate);
		element.values(values, FOUNDATION_REASON);
    }

	public void liquidation(WriterElement parent, Organisation values) throws Exception {
		WriterElement element = parent.create(URI, LIQUIDATION);
		datePartiallyKnownType(element, LIQUIDATION_DATE, values.liquidationDate);
		element.values(values, LIQUIDATION_REASON);
    }

	public void datePartiallyKnownType(WriterElement parent, String tagName, DatePartiallyKnown object) throws Exception {
		WriterEch0044.datePartiallyKnownType(parent, URI, tagName, object);
	}
	
}
