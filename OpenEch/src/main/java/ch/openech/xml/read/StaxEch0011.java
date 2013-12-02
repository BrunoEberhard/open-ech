package ch.openech.xml.read;


import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.openech.dm.code.FederalRegister;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Nationality;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.mj.model.EnumUtils;
import ch.openech.mj.model.properties.FlatProperties;
import ch.openech.mj.util.StringUtils;

import static ch.openech.xml.read.StaxEch.*;
import static ch.openech.dm.XmlConstants.*;

public class StaxEch0011 {

	public static void person(XmlPullParser xmlParser, Person person) throws XmlPullParserException, IOException {
		while (true) {
			int event = xmlParser.next();
			if (isEndTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(PERSON_IDENTIFICATION)) StaxEch0044.personIdentification(xmlParser, person.personIdentification);
				else if (startName.equals(COREDATA)) coredata(xmlParser, person);
				else skip(xmlParser);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
	public static Place birthplace(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
		Place place = new Place();
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(UNKNOWN)) skip(xmlParser);
				else if (startName.equals(SWISS_TOWN)) swissTown(xmlParser, place);
				else if (startName.equals(FOREIGN_COUNTRY)) foreignCountry(xmlParser, place);
				else skip(xmlParser);
			} else if (isEndTag(event)) return place;
			// else skip
		}
	}
	
	public static Place destination(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		Place place = new Place();
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(UNKNOWN)) skip(xmlParser);
				// Der Typ von swissTown ist unterschiedlich zwischen birthplace und destination
				else if (startName.equals(SWISS_TOWN)) StaxEch0007.municipality(xmlParser, place.municipalityIdentification);
				else if (startName.equals(FOREIGN_COUNTRY)) foreignCountry(xmlParser, place);
				else if (startName.equals(MAIL_ADDRESS)) place.mailAddress = StaxEch0010.address(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return place;
			}  // else skip
		}
	}
	
	private static void swissTown(XmlPullParser xmlParser, Place birthplace) throws XmlPullParserException, IOException {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(COUNTRY)) skip(xmlParser); //allways CH
				else if (startName.equals(MUNICIPALITY)) StaxEch0007.municipality(xmlParser, birthplace.municipalityIdentification);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			}  // else skip
		}
	}

	private static void foreignCountry(XmlPullParser xmlParser, Place place) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(COUNTRY)) StaxEch0008.country(xmlParser, place.countryIdentification);
				else if (startName.equals(FOREIGN_BIRTH_TOWN) || startName.equals(TOWN)) place.foreignTown = token(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			}  // else skip
		}
	}
	
	public static DwellingAddress dwellingAddress(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		DwellingAddress dwelingAddress = new DwellingAddress();
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(_E_G_I_D)) dwelingAddress.EGID = token(xmlParser);
				else if (startName.equals(_E_W_I_D)) dwelingAddress.EWID = token(xmlParser);
				else if (startName.equals(HOUSEHOLD_I_D)) dwelingAddress.householdID = token(xmlParser);
				else if (startName.equals(WITHOUT_E_G_I_D)) withoutEGID(xmlParser, dwelingAddress);
				else if (startName.equals(ADDRESS)) dwelingAddress.mailAddress = StaxEch0010.address(xmlParser);
				else if (startName.equals(TYPE_OF_HOUSEHOLD)) dwelingAddress.typeOfHousehold = token(xmlParser);
				else if (startName.equals(MOVING_DATE)) dwelingAddress.movingDate = date(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return dwelingAddress;
			}  // else skip
		}
	}
	
	public static void withoutEGID(XmlPullParser xmlParser, DwellingAddress dwelingAddress) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(HOUSEHOLD_I_D)) dwelingAddress.householdID = token(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			}  // else skip
		}
	}
	
	public static void maritalData(XmlPullParser xmlParser, Person person) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(MARITAL_STATUS)) enuum(xmlParser, person, Person.PERSON.maritalStatus.maritalStatus);
				else if (startName.equals(DATE_OF_MARITAL_STATUS)) person.maritalStatus.dateOfMaritalStatus = date(xmlParser);
				else if (startName.equals(SEPARATION)) enuum(xmlParser, person, Person.PERSON.separation.separation);
				else if (startName.equals(DATE_OF_SEPARATION)) person.separation.dateOfSeparation = date(xmlParser);
				else if (startName.equals(SEPARATION_TILL)) person.separation.separationTill = date(xmlParser);
				else if (startName.equals(CANCELATION_REASON)) enuum(xmlParser, person, Person.PERSON.cancelationReason);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}

	public static void coredata(XmlPullParser xmlParser, Person person) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String name = xmlParser.getName();
				if (StringUtils.equals(name, ORIGINAL_NAME, ALLIANCE_PARTNERSHIP_NAME,ALIAS_NAME, OTHER_NAME, CALL_NAME)) FlatProperties.set(person, name, token(xmlParser));
				else if (name.equals(PLACE_OF_BIRTH)) person.placeOfBirth = birthplace(xmlParser);
				else if (name.equals(DATE_OF_DEATH)) person.personIdentification.dateOfBirth = date(xmlParser);
				else if (name.equals(MARITAL_DATA)) maritalData(xmlParser, person);
				else if (name.equals(NATIONALITY)) nationality(xmlParser, person.nationality);
				else if (name.equals(CONTACT)) contact(xmlParser, person);
				else if (StringUtils.equals(name, LANGUAGE_OF_CORRESPONDANCE)) enuum(xmlParser, person, Person.PERSON.languageOfCorrespondance);
				else if (StringUtils.equals(name, RELIGION)) enuum(xmlParser, person, Person.PERSON.religion);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
	public static void nationality(XmlPullParser xmlParser, Nationality nationality) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(NATIONALITY_STATUS)) enuum(xmlParser, nationality, Nationality.NATIONALITY.nationalityStatus);
				else if (startName.equals(COUNTRY)) StaxEch0008.country(xmlParser, nationality.nationalityCountry);
				else skip(xmlParser);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
	public static void contact(XmlPullParser xmlParser, Person person) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(PERSON_IDENTIFICATION) || //
						startName.equals(PERSON_IDENTIFICATION_PARTNER) || //
						startName.equals(PARTNER_ID_ORGNISATION)) person.contactPerson.person = StaxEch0044.personIdentification(xmlParser);
				else if (startName.equals(CONTACT_ADDRESS)) person.contactPerson.address = StaxEch0010.address(xmlParser);
				else if (startName.equals(CONTACT_VALID_TILL)) person.contactPerson.validTill = date(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			}  // else skip
		}
	}
	
	public static PlaceOfOrigin placeOfOrigin(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		placeOfOrigin(xmlParser, placeOfOrigin);
		return placeOfOrigin;
	}
	
	public static void placeOfOrigin(XmlPullParser xmlParser, PlaceOfOrigin placeOfOrigin) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(ORIGIN_NAME)) placeOfOrigin.originName = token(xmlParser);
				else if (startName.equals(CANTON)) placeOfOrigin.cantonAbbreviation.canton = token(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	public static void anyPerson(XmlPullParser xmlParser, Person person) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(SWISS)) swiss(xmlParser, person);
				else if (startName.equals(FOREIGNER)) foreigner(xmlParser, person.foreign);
				else skip(xmlParser);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
	public static void swiss(XmlPullParser xmlParser, Person person) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(PLACE_OF_ORIGIN)) {
					PlaceOfOrigin placeOfOrigin = placeOfOrigin(xmlParser);
					// eventuell wurden diese Daten schon mit AddOn geliefert, in dem Fall werden sie ignoriert
					boolean alreadyKnown = false;
					for (PlaceOfOrigin p : person.placeOfOrigin) {
						if (StringUtils.equals(placeOfOrigin.cantonAbbreviation.canton, p.cantonAbbreviation.canton) && StringUtils.equals(placeOfOrigin.originName, p.originName)) {
							alreadyKnown = true; break;
						}
					}
					if (!alreadyKnown) {
						person.placeOfOrigin.add(placeOfOrigin);
					}
				}
				else skip(xmlParser);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
	public static void foreigner(XmlPullParser xmlParser, Foreign foreign) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(RESIDENCE_PERMIT)) enuum(xmlParser, foreign, Foreign.FOREIGN.residencePermit);
				else if (startName.equals(RESIDENCE_PERMIT_TILL)) foreign.residencePermitTill = date(xmlParser);
				else if (startName.equals(NAME_ON_PASSPORT)) foreign.nameOnPassport = token(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
	// bei allen residence - Methoden wird das "Type" nicht weggelassen
	
	public static void mainResidenceType(XmlPullParser xmlParser, Person person) throws XmlPullParserException, IOException {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(MAIN_RESIDENCE)) residence(xmlParser, person, true);
				else if (startName.equals(SECONDARY_RESIDENCE)) person.residence.secondary.add(StaxEch0007.municipality(xmlParser));
				else skip(xmlParser);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
	private static void residence(XmlPullParser xmlParser, Person person, boolean main) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (StringUtils.equals(startName, REPORTING_MUNICIPALITY, FEDERAL_REGISTER)) {
					MunicipalityIdentification municipalityIdentification;
					if (StringUtils.equals(startName, REPORTING_MUNICIPALITY)) {
						municipalityIdentification = StaxEch0007.municipality(xmlParser);
					} else {
						municipalityIdentification = new MunicipalityIdentification();
						Integer federRegister = integer(xmlParser);
						municipalityIdentification.historyMunicipalityId = -federRegister;
						municipalityIdentification.municipalityName = EnumUtils.getText(FederalRegister.values()[federRegister-1]);
					}
					if (main) {
						person.residence.reportingMunicipality = municipalityIdentification;
					} else {
						person.residence.secondary.add(municipalityIdentification);
					}
				}
				else if (startName.equals(ARRIVAL_DATE)) person.arrivalDate = date(xmlParser);
				else if (startName.equals(COMES_FROM)) person.comesFrom = destination(xmlParser);
				else if (startName.equals(DWELLING_ADDRESS)) person.dwellingAddress = StaxEch0011.dwellingAddress(xmlParser);
				else if (startName.equals(DEPARTURE_DATE)) person.departureDate = StaxEch.date(xmlParser);
				else if (startName.equals(GOES_TO)) person.goesTo = destination(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
	public static void secondaryResidenceType(XmlPullParser xmlParser, Person person) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				// der Typ von mainResindence ist bei mainResidenceType anders als bei secondaryResidenceType
				if (startName.equals(MAIN_RESIDENCE)) person.residence.reportingMunicipality = StaxEch0007.municipality(xmlParser);
				else if (startName.equals(SECONDARY_RESIDENCE)) residence(xmlParser, person, false);
				else skip(xmlParser);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
	public static void otherResidenceType(XmlPullParser xmlParser, Person person) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String tagName = xmlParser.getName();
				if (StringUtils.equals(tagName, SECONDARY_RESIDENCE_INFORMATION, SECONDARY_RESIDENCE)) {
					residence(xmlParser, person, false);
				} else {
					skip(xmlParser);
				}
			}
		}
	}
	
}
