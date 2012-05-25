package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.ADDRESS;
import static ch.openech.dm.XmlConstants.ALIAS_NAME;
import static ch.openech.dm.XmlConstants.ALLIANCE_PARTNERSHIP_NAME;
import static ch.openech.dm.XmlConstants.ANY_PERSON;
import static ch.openech.dm.XmlConstants.ARRIVAL_DATE;
import static ch.openech.dm.XmlConstants.CALL_NAME;
import static ch.openech.dm.XmlConstants.CANCELATION_REASON;
import static ch.openech.dm.XmlConstants.CANTON;
import static ch.openech.dm.XmlConstants.COMES_FROM;
import static ch.openech.dm.XmlConstants.CONTACT;
import static ch.openech.dm.XmlConstants.CONTACT_ADDRESS;
import static ch.openech.dm.XmlConstants.CONTACT_VALID_TILL;
import static ch.openech.dm.XmlConstants.COREDATA;
import static ch.openech.dm.XmlConstants.COUNTRY;
import static ch.openech.dm.XmlConstants.DATE_OF_DEATH;
import static ch.openech.dm.XmlConstants.DATE_OF_MARITAL_STATUS;
import static ch.openech.dm.XmlConstants.DATE_OF_SEPARATION;
import static ch.openech.dm.XmlConstants.DEPARTURE_DATE;
import static ch.openech.dm.XmlConstants.DWELLING_ADDRESS;
import static ch.openech.dm.XmlConstants.FOREIGNER;
import static ch.openech.dm.XmlConstants.FOREIGN_BIRTH_TOWN;
import static ch.openech.dm.XmlConstants.FOREIGN_COUNTRY;
import static ch.openech.dm.XmlConstants.GOES_TO;
import static ch.openech.dm.XmlConstants.HAS_MAIN_RESIDENCE;
import static ch.openech.dm.XmlConstants.HAS_OTHER_RESIDENCE;
import static ch.openech.dm.XmlConstants.HAS_SECONDARY_RESIDENCE;
import static ch.openech.dm.XmlConstants.HOUSEHOLD_I_D;
import static ch.openech.dm.XmlConstants.LANGUAGE_OF_CORRESPONDANCE;
import static ch.openech.dm.XmlConstants.MAIL_ADDRESS;
import static ch.openech.dm.XmlConstants.MAIN_RESIDENCE;
import static ch.openech.dm.XmlConstants.MARITAL_DATA;
import static ch.openech.dm.XmlConstants.MARITAL_STATUS;
import static ch.openech.dm.XmlConstants.MOVING_DATE;
import static ch.openech.dm.XmlConstants.MUNICIPALITY;
import static ch.openech.dm.XmlConstants.NATIONALITY;
import static ch.openech.dm.XmlConstants.NATIONALITY_STATUS;
import static ch.openech.dm.XmlConstants.ORIGINAL_NAME;
import static ch.openech.dm.XmlConstants.ORIGIN_NAME;
import static ch.openech.dm.XmlConstants.OTHER_NAME;
import static ch.openech.dm.XmlConstants.PERSON;
import static ch.openech.dm.XmlConstants.PLACE_OF_BIRTH;
import static ch.openech.dm.XmlConstants.PLACE_OF_ORIGIN;
import static ch.openech.dm.XmlConstants.RELIGION;
import static ch.openech.dm.XmlConstants.REPORTING_MUNICIPALITY;
import static ch.openech.dm.XmlConstants.SECONDARY_RESIDENCE;
import static ch.openech.dm.XmlConstants.SECONDARY_RESIDENCE_INFORMATION;
import static ch.openech.dm.XmlConstants.SEPARATION;
import static ch.openech.dm.XmlConstants.SEPARATION_TILL;
import static ch.openech.dm.XmlConstants.SWISS;
import static ch.openech.dm.XmlConstants.SWISS_TOWN;
import static ch.openech.dm.XmlConstants.TOWN;
import static ch.openech.dm.XmlConstants.TYPE_OF_HOUSEHOLD;
import static ch.openech.dm.XmlConstants.UNKNOWN;
import static ch.openech.dm.XmlConstants.WITHOUT_E_G_I_D;
import static ch.openech.dm.XmlConstants._E_G_I_D;
import static ch.openech.dm.XmlConstants._E_W_I_D;

import java.util.List;
import java.util.logging.Logger;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Nationality;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.mj.util.StringUtils;

public class WriterEch0011 {
	private static final Logger logger = Logger.getLogger(WriterEch0011.class.getName());

	private final EchSchema context;
	
	public final String URI;
	public final WriterEch0007 ech7;
	public final WriterEch0008 ech8;
	public final WriterEch0010 ech10;
	public final WriterEch0044 ech44;
	
	public WriterEch0011(EchSchema context) {
		this.context = context;
		
		URI = context.getNamespaceURI(11);
		ech7 = new WriterEch0007(context);
		ech8 = new WriterEch0008(context);
		ech10 = new WriterEch0010(context);
		ech44 = new WriterEch0044(context);
	}
	
	public void country(WriterElement parent, String tagName, CountryIdentification countryIdentification) throws Exception {
		if (countryIdentification == null || countryIdentification.isEmpty()) return;
		WriterElement writer = parent.create(URI, tagName);
    	writer.values(countryIdentification);
	}
	
	public void coreData(WriterElement parent, Person person) throws Exception {
		WriterElement coredata = parent.create(URI, COREDATA);
        
		coredata.values(person, ORIGINAL_NAME, ALLIANCE_PARTNERSHIP_NAME, ALIAS_NAME, OTHER_NAME, CALL_NAME);
        placeOfBirth(coredata, person);
        coredata.values(person, DATE_OF_DEATH);
        maritalData(coredata, person);
        nationality(coredata, person.nationality);
        contact(coredata, URI, person);
        coredata.values(person, LANGUAGE_OF_CORRESPONDANCE, RELIGION);
	}

	public void placeOfBirth(WriterElement parent, Person values) throws Exception {
		placeOfBirth(parent, values, URI);
	}
	
	public void placeOfBirth(WriterElement parent, Person values, String uri) throws Exception {
		Place placeOfBirth = values.placeOfBirth;
        
		WriterElement element =parent.create(uri, PLACE_OF_BIRTH);
        if (placeOfBirth == null || placeOfBirth.isUnknown()) {
        	unknown(element);
        } else if (placeOfBirth.isSwiss()) {
        	WriterElement swissTown =element.create(uri, SWISS_TOWN);
        	swissTown.text(COUNTRY, "CH");
            ech7.municipality(swissTown, MUNICIPALITY, placeOfBirth.municipalityIdentification);
        } else if (placeOfBirth.isForeign()) {
        	WriterElement foreignCountry = element.create(uri, FOREIGN_COUNTRY);
            ech8.country(foreignCountry, COUNTRY, placeOfBirth.countryIdentification);
            foreignCountry.text(FOREIGN_BIRTH_TOWN, placeOfBirth.foreignTown);
        }
	}

	public void maritalData(WriterElement parent, Person person) throws Exception {
		maritalData(parent, MARITAL_DATA, person);
	}
	
	public void maritalData(WriterElement parent, String tagName, Person person) throws Exception {
		WriterElement maritalData = parent.create(URI, tagName);
		maritalData.values(person.maritalStatus, MARITAL_STATUS, DATE_OF_MARITAL_STATUS);
		if (context.separationTillAvailable()) {
			maritalData.values(person.separation, SEPARATION, DATE_OF_SEPARATION, SEPARATION_TILL);
		} else {
			maritalData.values(person.separation, SEPARATION, DATE_OF_SEPARATION);
		}
		maritalData.values(person, CANCELATION_REASON);
	}

	public void nationality(WriterElement parent, Nationality nationality) throws Exception {
		WriterElement element = parent.create(URI, NATIONALITY);
		element.text(NATIONALITY_STATUS, nationality.nationalityStatus);
        ech8.country(element,  COUNTRY, nationality.nationalityCountry);
	}

	public void person(WriterElement parent, Person person) throws Exception {
		WriterElement element = parent.create(URI, PERSON);
		
		ech44.personIdentification(element, person.personIdentification);
        coreData(element, person);
	}
    
	public void anyPerson(WriterElement parent, Person person) throws Exception {
		WriterElement anyPerson = parent.create(URI, ANY_PERSON);
		
		List<PlaceOfOrigin> origins = person.placeOfOrigin;
		if (origins != null && !origins.isEmpty()) {
			swiss(anyPerson, origins);
			if (person.foreign != null && !StringUtils.isBlank(person.foreign.residencePermit)) {
				logger.warning("Person: " +person + " has swiss and foreign. Skipping foreign");
				return;
			}
		}
		
		if (person.foreign != null && !StringUtils.isBlank(person.foreign.residencePermit)) {
			foreigner(anyPerson, person.foreign);
		}
	}

	private void swiss(WriterElement anyPerson, List<PlaceOfOrigin> origins) throws Exception {
		WriterElement swiss = anyPerson.create(URI, SWISS);
		for (PlaceOfOrigin origin : origins) {
			placeOfOrigin(swiss, origin);
		}
	}

	private void placeOfOrigin(WriterElement parent, PlaceOfOrigin origin) throws Exception {
		placeOfOrigin(parent, PLACE_OF_ORIGIN, origin);
	}
	
	public void placeOfOrigin(WriterElement parent, String tagName, PlaceOfOrigin origin) throws Exception {
		WriterElement placeOfOrigin = parent.create(URI, tagName);
		placeOfOrigin.values(origin, ORIGIN_NAME, CANTON);
	}

	private void foreigner(WriterElement anyPerson, Foreign foreignValues) throws Exception {
		WriterElement foreign = anyPerson.create(URI, FOREIGNER);
		foreign.values(foreignValues);
	}
	
	// den Contact"Type" gibt es es als anonymen Type in verschiedenen
	// Schemas. In Java wird immer dieser aufgerufen (z.B. von ech20)
	// dafür muss die URI übergeben werden, damit der NS korrekt gemacht wird
	public void contact(WriterElement parent, String uri, Person person) throws Exception {
		contact(parent, uri, person, false);
	}
	
	public void contact(WriterElement parent, String uri, Person person, boolean withValidTill) throws Exception {
		if (person.contactPerson.person == null && (person.contactPerson.address == null || person.contactPerson.address.isEmpty())) return;
			
		WriterElement contactElement = parent.create(uri, CONTACT);
		// Hier würden 3 verschieden Identifikationsarten unterstützt:
		// eCH-0011:partnerIdOrganisationType
		// eCH-0044:personIdentificationPartnerType
		// eCH-0044:personIdentificationType
		if (person.contactPerson.person != null) ech44.personIdentification(contactElement, person.contactPerson.person);
		ech10.address(contactElement, CONTACT_ADDRESS, person.contactPerson.address);
		if (withValidTill && !StringUtils.isBlank(person.contactPerson.validTill)) contactElement.text(CONTACT_VALID_TILL, person.contactPerson.validTill);
	}

	public void residence(WriterElement message, Person values) throws Exception {
		if ("1".equals(values.typeOfResidence)) mainResidence(message, HAS_MAIN_RESIDENCE, values);
		else if ("2".equals(values.typeOfResidence)) secondaryResidence(message, HAS_SECONDARY_RESIDENCE, values);
		else if ("3".equals(values.typeOfResidence)) otherResidence(message, HAS_OTHER_RESIDENCE, values);
	}

	public void mainResidence(WriterElement parent, String tagName, Person values) throws Exception {
		// Im Fall der Niederlassung hängen die Informationen an der MainResidence und
		// es kommen n Aufenthaltsorte (SecondaryResidence) dazu
		
		WriterElement element = parent.create(URI, tagName);

		WriterElement informationElement = element.create(URI, MAIN_RESIDENCE);
		ech7.municipality(informationElement, REPORTING_MUNICIPALITY, values.residence.reportingMunicipality);
		commonResidenceInformation(informationElement, values);
		
		if (values.residence.secondary != null) {
			for (MunicipalityIdentification residence : values.residence.secondary) {
				ech7.municipality(element, SECONDARY_RESIDENCE, residence);
			}
		}
	}

	public void secondaryResidence(WriterElement parent, String tagName, Person values) throws Exception {
		// Im Fall des Aufenthalts hängen die Informationen an der (ersten und einzigen) SecondaryResidence
		// und es wird vorgänge die Niederlassung (MainResidence) eingefügt.
		
		WriterElement element = parent.create(URI, tagName);
		ech7.municipality(element, MAIN_RESIDENCE, values.residence.reportingMunicipality);
		
		WriterElement informationElement = element.create(URI, SECONDARY_RESIDENCE);
		if (values.residence.secondary != null && !values.residence.secondary.isEmpty()) {
			ech7.municipality(informationElement, REPORTING_MUNICIPALITY, values.residence.secondary.get(0));
		}
		commonResidenceInformation(informationElement, values);
	}
	
	public void otherResidence(WriterElement parent, String tagName, Person values) throws Exception {
		WriterElement element = parent.create(URI, tagName);
		WriterElement informationElement = element.create(URI, SECONDARY_RESIDENCE_INFORMATION); // "Information" ist eine Abweichung von der e20 - Version
		if (values.residence.secondary != null && !values.residence.secondary.isEmpty()) {
			ech7.municipality(informationElement, REPORTING_MUNICIPALITY, values.residence.secondary.get(0));
		}
		commonResidenceInformation(informationElement, values);
	}
	
	private void commonResidenceInformation(WriterElement element, Person values) throws Exception {
		element.values(values, ARRIVAL_DATE);
		destination(element, COMES_FROM, values.comesFrom, true);
		dwellingAddress(element, values.dwellingAddress);
		element.values(values, DEPARTURE_DATE);
		destination(element, GOES_TO, values.goesTo, false);
	}

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
	
	public void dwellingAddress(WriterElement parent, DwellingAddress dwelingAddress) throws Exception {
		dwellingAddress(parent, URI, DWELLING_ADDRESS, dwelingAddress);
	}
	
	public void dwellingAddress(WriterElement parent, String uri, String tagName, DwellingAddress dwelingAddress) throws Exception {
		if (dwelingAddress == null) return; 
		
		WriterElement element = parent.create(uri, tagName);
		if (!StringUtils.isBlank(dwelingAddress.EGID)) {
			element.values(dwelingAddress, _E_G_I_D, _E_W_I_D, HOUSEHOLD_I_D);
		} else {
			WriterElement withoutEGID = element.create(URI, WITHOUT_E_G_I_D);
			withoutEGID.text(HOUSEHOLD_I_D, dwelingAddress.householdID); 
		}
		if (dwelingAddress.mailAddress != null && !dwelingAddress.mailAddress.isEmpty()) ech10.swissAddressInformation(element, ADDRESS, dwelingAddress.mailAddress);
		element.values(dwelingAddress, TYPE_OF_HOUSEHOLD, MOVING_DATE);
	}
	
	private static void unknown(WriterElement parent) throws Exception {
		parent.text(UNKNOWN, "0");
	}
	
}
