package ch.openech.xml.read;


import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.Place;
import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Nationality;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;

public class StaxEch0011 {

	public static void person(Person person) {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(PERSON_IDENTIFICATION)) StaxEch0044.personIdentification(xml, person.personIdentification);
//				else if (startName.equals(COREDATA)) coredata(xml, person);
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
	public static Place birthplace() {
		Place place = new Place();
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(UNKNOWN)) skip(xml);
//				else if (startName.equals(SWISS_TOWN)) swissTown(xml, place);
//				else if (startName.equals(FOREIGN_COUNTRY)) foreignCountry(xml, place);
//				else skip(xml);
//			} else if (event.isEndElement()) return place;
//			// else skip
//		}
		return place;
	}
	
	public static Place destination()  {
		Place place = new Place();
		return place;
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(UNKNOWN)) skip(xml);
//				// Der Typ von swissTown ist unterschiedlich zwischen birthplace und destination
//				else if (startName.equals(SWISS_TOWN)) StaxEch0007.municipality(xml, place.municipalityIdentification);
//				else if (startName.equals(FOREIGN_COUNTRY)) foreignCountry(xml, place);
//				else if (startName.equals(MAIL_ADDRESS)) place.mailAddress = StaxEch0010.address(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return place;
//			}  // else skip
//		}
	}
	
	private static void swissTown(Place birthplace) {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(COUNTRY)) skip(xml); //allways CH
//				else if (startName.equals(MUNICIPALITY)) StaxEch0007.municipality(xml, birthplace.municipalityIdentification);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			}  // else skip
//		}
	}

	private static void foreignCountry(Place place)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(COUNTRY)) StaxEch0008.country(xml, place.countryIdentification);
//				else if (startName.equals(FOREIGN_BIRTH_TOWN) || startName.equals(TOWN)) place.foreignTown = token(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			}  // else skip
//		}
	}
	
	public static DwellingAddress dwellingAddress()  {
		DwellingAddress dwelingAddress = new DwellingAddress();
		return dwelingAddress;
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(_E_G_I_D)) dwelingAddress.EGID = token(xml);
//				else if (startName.equals(_E_W_I_D)) dwelingAddress.EWID = token(xml);
//				else if (startName.equals(HOUSEHOLD_I_D)) dwelingAddress.householdID = token(xml);
//				else if (startName.equals(WITHOUT_E_G_I_D)) withoutEGID(xml, dwelingAddress);
//				else if (startName.equals(ADDRESS)) dwelingAddress.mailAddress = StaxEch0010.address(xml);
//				else if (startName.equals(TYPE_OF_HOUSEHOLD)) dwelingAddress.typeOfHousehold = token(xml);
//				else if (startName.equals(MOVING_DATE)) dwelingAddress.movingDate = date(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return dwelingAddress;
//			}  // else skip
//		}
	}
	
	public static void withoutEGID(DwellingAddress dwelingAddress)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(HOUSEHOLD_I_D)) dwelingAddress.householdID = token(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			}  // else skip
//		}
	}
	
	public static void maritalData(Person person)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(MARITAL_STATUS)) enuum(xml, person, Person.PERSON.maritalStatus.maritalStatus);
//				else if (startName.equals(DATE_OF_MARITAL_STATUS)) person.maritalStatus.dateOfMaritalStatus = date(xml);
//				else if (startName.equals(SEPARATION)) enuum(xml, person, Person.PERSON.separation.separation);
//				else if (startName.equals(DATE_OF_SEPARATION)) person.separation.dateOfSeparation = date(xml);
//				else if (startName.equals(SEPARATION_TILL)) person.separation.separationTill = date(xml);
//				else if (startName.equals(CANCELATION_REASON)) enuum(xml, person, Person.PERSON.cancelationReason);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}

	public static void coredata(Person person)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement element = event.asStartElement();
//				String name = element.getName().getLocalPart();
//				if (StringUtils.equals(name, ORIGINAL_NAME, ALLIANCE_PARTNERSHIP_NAME,ALIAS_NAME, OTHER_NAME, CALL_NAME)) FlatProperties.set(person, name, token(xml));
//				else if (name.equals(PLACE_OF_BIRTH)) person.placeOfBirth = birthplace(xml);
//				else if (name.equals(DATE_OF_DEATH)) person.personIdentification.dateOfBirth = date(xml);
//				else if (name.equals(MARITAL_DATA)) maritalData(xml, person);
//				else if (name.equals(NATIONALITY)) nationality(xml, person.nationality);
//				else if (name.equals(CONTACT)) contact(xml, person);
//				else if (StringUtils.equals(name, LANGUAGE_OF_CORRESPONDANCE)) enuum(xml, person, Person.PERSON.languageOfCorrespondance);
//				else if (StringUtils.equals(name, RELIGION)) enuum(xml, person, Person.PERSON.religion);
//					
////				if (name.equals(ORIGINAL_NAME)) person.originalName = token(xml);
////				else if (name.equals(ALLIANCE_PARTNERSHIP_NAME)) person.alliancePartnershipName = token(xml);
////				else if (name.equals(ALIAS_NAME)) person.aliasName = token(xml);
////				else if (name.equals(OTHER_NAME)) person.otherName = token(xml);
////				else if (name.equals(CALL_NAME)) person.callName = token(xml);
////				else if (name.equals(PLACE_OF_BIRTH)) person.placeOfBirth = birthplace(xml);
////				else if (name.equals(DATE_OF_DEATH))person.setDateOfBirth(date(xml));
////				else if (name.equals(MARITAL_DATA)) maritalData(xml, person);
////				else if (name.equals(NATIONALITY)) nationality(xml, person.nationality);
////				else if (name.equals(CONTACT)) contact(xml, person);
////				else if (name.equals(LANGUAGE_OF_CORRESPONDANCE)) enuum(xml, person, Person.PERSON.languageOfCorrespondance);
////				else if (name.equals(RELIGION)) person.religion = token(xml);
////				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
		//}
	}
	
	public static void nationality(Nationality nationality)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(NATIONALITY_STATUS)) enuum(xml, nationality, Nationality.NATIONALITY.nationalityStatus);
//				else if (startName.equals(COUNTRY)) StaxEch0008.country(xml, nationality.nationalityCountry);
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
	public static void contact(Person person)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(PERSON_IDENTIFICATION) || //
//						startName.equals(PERSON_IDENTIFICATION_PARTNER) || //
//						startName.equals(PARTNER_ID_ORGNISATION)) person.contactPerson.person = StaxEch0044.personIdentification(xml);
//				else if (startName.equals(CONTACT_ADDRESS)) person.contactPerson.address = StaxEch0010.address(xml);
//				else if (startName.equals(CONTACT_VALID_TILL)) person.contactPerson.validTill = date(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			}  // else skip
//		}
	}
	
	public static PlaceOfOrigin placeOfOrigin()  {
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		// placeOfOrigin(xml, placeOfOrigin);
		return placeOfOrigin;
	}
	
	public static void placeOfOrigin(PlaceOfOrigin placeOfOrigin)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(ORIGIN_NAME)) placeOfOrigin.originName = token(xml);
//				else if (startName.equals(CANTON)) placeOfOrigin.cantonAbbreviation.canton = token(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) {
//				return;
//			} // else skip
//		}
	}
	
	public static void anyPerson(Person person)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(SWISS)) swiss(xml, person);
//				else if (startName.equals(FOREIGNER)) foreigner(xml, person.foreign);
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
	public static void swiss(Person person)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(PLACE_OF_ORIGIN)) {
//					PlaceOfOrigin placeOfOrigin = placeOfOrigin(xml);
//					// eventuell wurden diese Daten schon mit AddOn geliefert, in dem Fall werden sie ignoriert
//					boolean alreadyKnown = false;
//					for (PlaceOfOrigin p : person.placeOfOrigin) {
//						if (StringUtils.equals(placeOfOrigin.cantonAbbreviation.canton, p.cantonAbbreviation.canton) && StringUtils.equals(placeOfOrigin.originName, p.originName)) {
//							alreadyKnown = true; break;
//						}
//					}
//					if (!alreadyKnown) {
//						person.placeOfOrigin.add(placeOfOrigin);
//					}
//				}
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
	public static void foreigner(Foreign foreign)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(RESIDENCE_PERMIT)) enuum(xml, foreign, Foreign.FOREIGN.residencePermit);
//				else if (startName.equals(RESIDENCE_PERMIT_TILL)) foreign.residencePermitTill = date(xml);
//				else if (startName.equals(NAME_ON_PASSPORT)) foreign.nameOnPassport = token(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
	// bei allen residence - Methoden wird das "Type" nicht weggelassen
	
	public static void mainResidenceType(Person person) {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (startName.equals(MAIN_RESIDENCE)) residence(xml, person, true);
//				else if (startName.equals(SECONDARY_RESIDENCE)) person.residence.secondary.add(StaxEch0007.municipality(xml));
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
	private static void residence(Person person, boolean main)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (StringUtils.equals(startName, REPORTING_MUNICIPALITY, FEDERAL_REGISTER)) {
//					MunicipalityIdentification municipalityIdentification;
//					if (StringUtils.equals(startName, REPORTING_MUNICIPALITY)) {
//						municipalityIdentification = StaxEch0007.municipality(xml);
//					} else {
//						municipalityIdentification = new MunicipalityIdentification();
//						Integer federRegister = StaxEch.integer(xml);
//						municipalityIdentification.historyMunicipalityId = -federRegister;
//						municipalityIdentification.municipalityName = EnumUtils.getText(FederalRegister.values()[federRegister-1]);
//					}
//					if (main) {
//						person.residence.reportingMunicipality = municipalityIdentification;
//					} else {
//						person.residence.secondary.add(municipalityIdentification);
//					}
//				}
//				else if (startName.equals(ARRIVAL_DATE)) person.arrivalDate = StaxEch.date(xml);
//				else if (startName.equals(COMES_FROM)) person.comesFrom = destination(xml);
//				else if (startName.equals(DWELLING_ADDRESS)) person.dwellingAddress = StaxEch0011.dwellingAddress(xml);
//				else if (startName.equals(DEPARTURE_DATE)) person.departureDate = StaxEch.date(xml);
//				else if (startName.equals(GOES_TO)) person.goesTo = destination(xml);
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
	public static void secondaryResidenceType(Person person)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				// der Typ von mainResindence ist bei mainResidenceType anders als bei secondaryResidenceType
//				if (startName.equals(MAIN_RESIDENCE)) person.residence.reportingMunicipality = StaxEch0007.municipality(xml);
//				else if (startName.equals(SECONDARY_RESIDENCE)) residence(xml, person, false);
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
	public static void otherResidenceType(Person person)  {
//		while (true) {
//			XMLEvent event = xml.nextEvent();
//			if (event.isStartElement()) {
//				StartElement startElement = event.asStartElement();
//				String startName = startElement.getName().getLocalPart();
//				if (StringUtils.equals(startName, SECONDARY_RESIDENCE_INFORMATION, SECONDARY_RESIDENCE)) residence(xml, person, false);
//				else skip(xml);
//			} else if (event.isEndElement()) return;
//			// else skip
//		}
	}
	
}
