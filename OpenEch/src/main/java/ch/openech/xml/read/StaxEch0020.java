package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.enuum;
import static ch.openech.xml.read.StaxEch.isEndDocument;
import static ch.openech.xml.read.StaxEch.isEndTag;
import static ch.openech.xml.read.StaxEch.isStartTag;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.joda.time.LocalDateTime;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import ch.openech.dm.Event;
import ch.openech.dm.XmlConstants;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Relation;
import ch.openech.dm.person.types.ReasonOfAcquisition;
import ch.openech.dm.types.TypeOfResidence;
import ch.openech.dm.types.YesNo;
import ch.openech.mj.toolkit.ProgressListener;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;
import ch.openech.server.EchPersistence;


public class StaxEch0020 implements StaxEchParser {

	private final EchPersistence persistence;
	
	// hack: Globale Variable als 2. Rückgabewert von simplePersonEventPerson and simplePersonEvent
	// Dies ist notwendige, weil changeNamePersonType einerseits die Identifikation der zu ändernden
	// Person enthält, andererseits aber schon einen Teil (!) der neuen Werte.
	private Person personToChange = null;
	private Event e;
	private String lastInsertedPersonId;
	
	public StaxEch0020(EchPersistence persistence) {
		this.persistence = persistence;
	}
	
	// Persistence
	
	public void insertPerson(Person person) {
		person.event = e;
		if (person.getId() == null) {
			person.personIdentification.technicalIds.localId.setOpenEch();
		}
		persistence.person().insert(person);
		lastInsertedPersonId = person.getId();
	}

	@Override
	public String getLastInsertedId() {
		return lastInsertedPersonId;
	}

	public void simplePersonEvent(String type, PersonIdentification personIdentification, Person person) {
		if (StringUtils.equals(type, XmlConstants.DIVORCE, XmlConstants.UNDO_MARRIAGE, XmlConstants.UNDO_PARTNERSHIP)) removePartner(person);
		person.event = e;
		persistence.person().update(person);
		persistence.commit();
	}

	//
	
	private void removePartner(Person changedPerson) {
		for (int i = changedPerson.relation.size()-1; i>= 0; i--) {
			Relation relation = changedPerson.relation.get(i);
			if (relation.isPartnerType()) changedPerson.relation.remove(i);
		}
	}
	
	public Person getPerson(PersonIdentification personIdentification) {
		if (personIdentification.getId() != null) {
			return persistence.personLocalPersonIdIndex().find(personIdentification.getId());
		} else if (!StringUtils.isBlank(personIdentification.vn.value)) {
			return persistence.personVnIndex().find(personIdentification.vn.value);
		} else {
			return persistence.getByName(personIdentification.officialName, personIdentification.firstName, personIdentification.dateOfBirth);
		}
	}
	
	//
	
	@Override
	public void process(String xmlString) throws XmlPullParserException, IOException  {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xmlParser = factory.newPullParser();
		xmlParser.setInput(new StringReader(xmlString));
		process(xmlParser, xmlString, null);
	}

	public void process(InputStream inputStream, String eventString, ProgressListener progressListener) throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xmlParser = factory.newPullParser();
		xmlParser.setInput(new InputStreamReader(inputStream));
		process(xmlParser, eventString, progressListener);
	}
	

	private void process(XmlPullParser xmlParser, String xmlString, ProgressListener progressListener) throws XmlPullParserException, IOException  {
		while (!isEndDocument(xmlParser.getEventType())  && !isCanceled(progressListener)) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(DELIVERY)) {
					delivery(xmlString, xmlParser, progressListener);
				}
				else skip(xmlParser);
			} 
		}
	}
	
	private void delivery(String xmlString, XmlPullParser xmlParser, ProgressListener progressListener) throws XmlPullParserException, IOException  {
		while (!isCanceled(progressListener)) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(DELIVERY_HEADER)) {
					skip(xmlParser);
				} else {
					e = new Event();
					e.type = startName;
					// e.message = xmlString;
					e.time = new LocalDateTime();
					
					if (startName.equals(BASE_DELIVERY)) baseDelivery(xmlParser, progressListener);
					else if (startName.equals(BIRTH)) eventBirth(xmlParser);
					else if (startName.equals(MOVE_IN)) eventMoveIn(xmlParser);
					else if (StringUtils.equals(startName, NATURALIZE_FOREIGNER, NATURALIZE_SWISS, UNDO_CITIZEN)) eventNaturalize(startName, xmlParser);
					else simplePersonEvent(xmlParser, startName);
				}
			} else if (isEndTag(event)) return;
			// else skip
		}
	}
	
	private boolean isCanceled(ProgressListener progressListener) {
		return progressListener.isCanceled();
	}
	
	private void baseDelivery(XmlPullParser xmlParser,  ProgressListener progressListener) throws XmlPullParserException, IOException  {
		int numberOfMessages = 1;
		int count = 0;
		
		while (!isCanceled(progressListener)) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(MESSAGES)) {
					messages(xmlParser);
					if (progressListener != null) progressListener.showProgress(count++, numberOfMessages);
				}
				else if (startName.equals(NUMBER_OF_MESSAGES)) numberOfMessages = StaxEch.integer(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) return;
			// else skip
		}
	}

	public void messages(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		Person person = new Person();
		
		while(true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(BASE_DELIVERY_PERSON)) baseDeliveryPerson(xmlParser, person);
				else if (startName.equals(NAME_OF_FATHER)) StaxEch0021.nameOfParentAtBirth(xmlParser, person.nameOfParents.father);
				else if (startName.equals(NAME_OF_MOTHER)) StaxEch0021.nameOfParentAtBirth(xmlParser, person.nameOfParents.mother);
				else if (startName.equals(_RELATIONSHIP) || startName.equals(RELATIONSHIP)) StaxEch0021.relation(xmlParser, person);
				else if (startName.equals(OCCUPATION)) person.occupation.add(StaxEch0021.occupation(xmlParser));
				else if (startName.equals(DATA_LOCK)) person.dataLock = token(xmlParser);
				else if (startName.equals(PAPER_LOCK)) person.paperLock = token(xmlParser);
				else if (startName.equals(EXTENSION)) extension(xmlParser, person);
				// TODO householdNumber
				else residenceChoiceOrSkip(startName, xmlParser, person);
			} else if (isEndTag(event)) {
				insertPerson(person);	
				return;
			}
			// else skip
		}
	}
	
	public static void residenceChoiceOrSkip(String startName,XmlPullParser xmlParser,  Person person) throws XmlPullParserException, IOException  {
		if (startName.equals(HAS_MAIN_RESIDENCE)) {
			person.typeOfResidence = TypeOfResidence.hasMainResidence;
			StaxEch0011.mainResidenceType(xmlParser, person);
		} else if (startName.equals(HAS_SECONDARY_RESIDENCE)) {
			person.typeOfResidence = TypeOfResidence.hasSecondaryResidence;
			StaxEch0011.secondaryResidenceType(xmlParser, person);
		} else if (startName.equals(HAS_OTHER_RESIDENCE)) {
			person.typeOfResidence = TypeOfResidence.hasOtherResidence;
			StaxEch0011.otherResidenceType(xmlParser, person);
		} else {
			skip(xmlParser);
		}
	}

	public void extension(XmlPullParser xmlParser, Person personToChange) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (CONTACT.equals(startName)) personToChange.contact = StaxEch0046.contact(xmlParser);
				else if ("personExtendedInformation".equals(startName)) personToChange.personExtendedInformation = StaxEch0101.information(xmlParser);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	public void baseDeliveryPerson(XmlPullParser xmlParser, Person values) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(PERSON)) StaxEch0011.person(xmlParser, values);
				else if (startName.equals(ANY_PERSON)) StaxEch0011.anyPerson(xmlParser, values);
				else if (startName.equals(PLACE_OF_ORIGIN_ADDON)) StaxEch0021.addPlaceOfOriginAddon(xmlParser, values.placeOfOrigin);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}

	public void eventBirth(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		Person person = null;
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(BIRTH_PERSON)) person = newPerson(xmlParser);
				else if (startName.equals(MOTHER)) birthParent(false, xmlParser, person);
				else if (startName.equals(FATHER)) birthParent(true, xmlParser, person);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				completePlaceOfOrigins(person);
				copyResidence(person);
				insertPerson(person);
				return;
			} // else skip
		}
	}
	
	private void completePlaceOfOrigins(Person person) {
		for (PlaceOfOrigin placeOfOrigin : person.placeOfOrigin) {
			placeOfOrigin.naturalizationDate = DateUtils.convertToLocalDate(person.personIdentification.dateOfBirth);
			placeOfOrigin.reasonOfAcquisition = ReasonOfAcquisition.Abstammung;
		}
	}
	
	private void copyResidence(Person person) {
		Relation relation = person.getMother();
		if (relation == null) relation = person.getFather();
		if (relation == null) {
			// TODO was macht man hier? Einfach so entstehen ungültige Daten, da eine Person einen Meldeort haben muss
			return;
		}
		Person parent = getPerson(relation.partner);
		if (parent == null) return;
		person.typeOfResidence = parent.typeOfResidence;
		person.residence.reportingMunicipality = parent.residence.reportingMunicipality;
		person.residence.setSecondary(parent.residence.secondary);
	}
	
	// entspricht birthMother + birthFather
	public static void birthParent(boolean father,XmlPullParser xmlParser,  Person person) throws XmlPullParserException, IOException  {
		Relation relation = new Relation();
		
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(TYPE_OF_RELATIONSHIP)) enuum(xmlParser, relation, Relation.RELATION.typeOfRelationship);
				else if (startName.equals(PARTNER)) {
					birthPartner(xmlParser, relation);
					relation.care = YesNo.Yes;
					person.relation.add(relation);
				}
				else if (startName.equals(NAME_AT_BIRTH)) StaxEch0021.nameOfParentAtBirth(xmlParser, father ? person.nameOfParents.father : person.nameOfParents.mother);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	public static void birthPartner(XmlPullParser xmlParser, Relation relation) throws XmlPullParserException, IOException  {
		
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(PERSON_IDENTIFICATION) || //
						startName.equals(PERSON_IDENTIFICATION_PARTNER)) relation.partner = StaxEch0044.personIdentification(xmlParser);
				else if (startName.equals(ADDRESS)) relation.address = StaxEch0010.address(xmlParser) ;
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	public void eventMoveIn(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		Person person = null;
		
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(MOVE_IN_PERSON)) person = newPerson(xmlParser);
				else if (startName.equals(NAME_OF_FATHER)) StaxEch0021.nameOfParentAtBirth(xmlParser, person.nameOfParents.father);
				else if (startName.equals(NAME_OF_MOTHER)) StaxEch0021.nameOfParentAtBirth(xmlParser, person.nameOfParents.mother);
				else if (startName.equals(OCCUPATION)) person.occupation.add(StaxEch0021.occupation(xmlParser));
				else if (startName.equals(_RELATIONSHIP) || startName.equals(RELATIONSHIP)) StaxEch0021.relation(xmlParser, person);
				else if (startName.equals(EXTENSION)) extension(xmlParser, person);
				else residenceChoiceOrSkip(startName, xmlParser, person);
			} else if (isEndTag(event)) {
				insertPerson(person);
				return;
			} // else skip
		}
	}
	
	public void moveOutReportingDestination(XmlPullParser xmlParser, Person personToChange) throws XmlPullParserException, IOException {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(REPORTING_MUNICIPALITY)) personToChange.residence.reportingMunicipality = StaxEch0007.municipality(xmlParser);
				else if (startName.equals(FEDERAL_REGISTER)) personToChange.residence.reportingMunicipality = federalRegister(xmlParser);
				else if (startName.equals(GOES_TO)) personToChange.goesTo = StaxEch0011.destination(xmlParser);
				else if (startName.equals(DEPARTURE_DATE)) personToChange.departureDate = StaxEch.date(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	private MunicipalityIdentification federalRegister(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
		municipalityIdentification.historyMunicipalityId = -StaxEch.integer(xmlParser);
		return municipalityIdentification;
	}
	
	public void moveReportingAddress(XmlPullParser xmlParser, Person personToChange) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(REPORTING_MUNICIPALITY)) personToChange.residence.reportingMunicipality = StaxEch0007.municipality(xmlParser);
				else if (startName.equals(FEDERAL_REGISTER)) personToChange.residence.reportingMunicipality = federalRegister(xmlParser);
				else if (startName.equals(DWELLING_ADDRESS)) personToChange.dwellingAddress = StaxEch0011.dwellingAddress(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	// used as moveInPerson, birthPerson, changeResidenceType
	// also used in e93
	public static Person newPerson(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		Person person = new Person();
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String name = xmlParser.getName();
				if (name.equals(PERSON_IDENTIFICATION)) StaxEch0044.personIdentification(xmlParser, person.personIdentification);
				else if (name.equals(ORIGINAL_NAME)) person.originalName = token(xmlParser);
				else if (name.equals(ALLIANCE_PARTNERSHIP_NAME)) person.alliancePartnershipName = token(xmlParser);
				else if (name.equals(ALIAS_NAME)) person.aliasName = token(xmlParser);
				else if (name.equals(OTHER_NAME)) person.otherName = token(xmlParser);
				else if (name.equals(CALL_NAME)) person.callName = token(xmlParser);
				else if (name.equals(PLACE_OF_BIRTH)) person.placeOfBirth = StaxEch0011.birthplace(xmlParser);
				else if (name.equals(NATIONALITY)) StaxEch0011.nationality(xmlParser, person.nationality);
				else if (name.equals(CONTACT)) StaxEch0011.contact(xmlParser, person);
				else if (name.equals(RELIGION)) enuum(xmlParser, person, Person.PERSON.religion);
				else if (StringUtils.equals(name, LANGUAGE_OF_CORRESPONDANCE, "languageOfCorrespondence")) enuum(xmlParser, person, Person.PERSON.languageOfCorrespondance);
				else if (name.equals(MARITAL_DATA)) StaxEch0011.maritalData(xmlParser, person);
				else if (name.equals(ANY_PERSON)) StaxEch0011.anyPerson(xmlParser, person);
				else if (name.equals(PLACE_OF_ORIGIN_ADDON) || name.equals(CHANGE_RESIDENCE_TYPE_ORIGIN_ADD_ON)) StaxEch0021.addPlaceOfOriginAddon(xmlParser, person.placeOfOrigin);
				else skip(xmlParser);
			} else if (isEndTag(event)) return person;
			// else skip
		}
	}
	
	// Swiss & Foreign
	public void eventNaturalize(String eventName, XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		PersonIdentification personIdentification = null;
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.endsWith("Person")) personIdentification = simplePersonEventPerson(eventName, xmlParser);
				else if (StringUtils.equals(startName, PLACE_OF_ORIGIN)) StaxEch0011.placeOfOrigin(xmlParser, placeOfOrigin);
				else if (startName.equals(NATURALIZATION_DATE)) placeOfOrigin.naturalizationDate = StaxEch.date(xmlParser);
				else if (startName.equals(REASON_OF_ACQUISITION)) enuum(xmlParser, placeOfOrigin, PlaceOfOrigin.PLACE_OF_ORIGIN.reasonOfAcquisition);
				else if (startName.equals(EXPATRIATION_DATE)) placeOfOrigin.expatriationDate = StaxEch.date(xmlParser);
				else if (startName.equals(NATIONALITY)) StaxEch0011.nationality(xmlParser, personToChange.nationality);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				StaxEch0021.updatePlaceOfOrigin(personToChange.placeOfOrigin, placeOfOrigin);
				personToChange.foreign.residencePermit = null;
				personToChange.foreign.residencePermitTill = null;
				simplePersonEvent(eventName, personIdentification, personToChange);
				return;
			} // else skip
		}
	}
	
	public void changeResidenceTypeReportingMunicipality(XmlPullParser xmlParser, Person personToChange) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(REPORTING_MUNICIPALITY)) personToChange.residence.reportingMunicipality = StaxEch0007.municipality(xmlParser);
				else if (startName.equals(TYP_OF_RESIDENCE)) enuum(xmlParser, personToChange, Person.PERSON.typeOfResidence);
				else if (startName.equals(ARRIVAL_DATE)) personToChange.arrivalDate = StaxEch.date(xmlParser);
				else if (startName.equals(COMES_FROM)) personToChange.comesFrom = StaxEch0011.destination(xmlParser);
				else if (startName.equals(DWELLING_ADDRESS)) personToChange.dwellingAddress = StaxEch0011.dwellingAddress(xmlParser);
			} else if (isEndTag(event)) {
				return;
			} // else skip
		}
	}
	
	// correctOccupation etc
	private void simplePersonEvent(XmlPullParser xmlParser, String eventName) throws XmlPullParserException, IOException  {
		PersonIdentification personIdentification = null;
		personToChange = null;
		
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (StringUtils.equals(startName, eventName + "Person", CORRECT_PERSON, //
						NATURALIZATION_FOREIGNER_PERSON, CHANGE_RESIDENCE_PERMIT_PERSON, //
						GARDIAN_MEASURE_PERSON, CHANGE_OF_OCCUPATION_PERSON, //
						CONTACT_ADDRESS_PERSON, NATURALIZATION_SWISS_PERSON, CHANGE_OF_RELIGION_PERSON, //
						CITIZENSHIP_DISCHARGE_PERSON)) {
					personIdentification = simplePersonEventPerson(eventName, xmlParser);
					
					// Bei gewissen Events werden 1-n Beziehungen komplett geliefert (z.B. correctRelationship)
					// bei anderen werden nur die zusätzlichen geliefert (z.B. care)
					// Bei denen, wo alle, auch die unveränderten mitkommen, müssen die bisherigen Listen
					// gelöscht werden, damit die unveränderten Einträge nicht verdoppelt werden.
					
					if (CORRECT_OCCUPATION.equals(eventName) || CHANGE_RESIDENCE_TYPE.equals(eventName) || //
							CHANGE_RESIDENCE_PERMIT.equals(eventName) || RENEW_PERMIT.equals(eventName)) personToChange.occupation.clear();
					if (CORRECT_RELATIONSHIP.equals(eventName) || CHANGE_RESIDENCE_TYPE.equals(eventName)) personToChange.relation.clear();
					if (CHANGE_CITIZEN.equals(eventName) || UNDO_SWISS.equals(eventName) || CORRECT_ORIGIN.equals(eventName)) personToChange.placeOfOrigin.clear();
					if (GARDIAN_MEASURE.equals(eventName) || UNDO_GARDIAN.equals(eventName)  || CHANGE_GARDIAN.equals(eventName)) removeGardianRelationship();
					if (StringUtils.equals(eventName, UNDO_MISSING, CORRECT_DATE_OF_DEATH)) personToChange.dateOfDeath = null;
					if (UNDO_SEPARATION.equals(eventName)) {
						personToChange.separation.separation = null; personToChange.separation.separationTill = null; personToChange.separation.dateOfSeparation = null;
					}
					if (CORRECT_CONTACT.equals(eventName)) {
						personToChange.contactPerson.address = null; personToChange.contactPerson.person = null; personToChange.contactPerson.validTill = null;
					}
					if (CORRECT_REPORTING.equals(eventName)) personToChange.residence.secondary.clear();
				}
				else if (startName.equals(OCCUPATION)) personToChange.occupation.add(StaxEch0021.occupation(xmlParser));
				else if (startName.equals(_RELATIONSHIP) || startName.equals(RELATIONSHIP) || startName.equals(_RELATIONSHIP_TYPE) || //
						startName.equals(MARRIAGE_RELATIONSHIP) || startName.equals(PARTNERSHIP_RELATIONSHIP) || //
						startName.equals(CARE_RELATIONSHIP)) StaxEch0021.relation(xmlParser, personToChange);
				else if (startName.equals(PLACE_OF_ORIGIN_ADDON)) StaxEch0021.addPlaceOfOriginAddon(xmlParser, personToChange.placeOfOrigin);
				else if (StringUtils.equals(startName, PLACE_OF_ORIGIN)) {
					// Das ist bis jetzt der einzige Fall, wo ein Attribute gleich heisst, aber einen unterschiedlichen Typ hat.
					// (Eigentlich ist es in correctOrigin einfach falsch)
					if (CORRECT_ORIGIN.equals(eventName)) {
						StaxEch0021.addPlaceOfOriginAddon(xmlParser, personToChange.placeOfOrigin);
					} else {
						personToChange.placeOfOrigin.add(StaxEch0011.placeOfOrigin(xmlParser));
					}
				}
				else if (StringUtils.equals(startName, ORIGIN)) StaxEch0021.addPlaceOfOriginAddon(xmlParser, personToChange.placeOfOrigin);
				else if (startName.equals(EXPATRIATION_DATE)) personToChange.placeOfOrigin.get(0).expatriationDate = StaxEch.date(xmlParser);
				else if (startName.equals(DATE_OF_DEATH)) personToChange.dateOfDeath = StaxEch.date(xmlParser);
				else if (startName.equals(NAME_OF_FATHER)) StaxEch0021.nameOfParentAtBirth(xmlParser, personToChange.nameOfParents.father);
				else if (startName.equals(NAME_OF_MOTHER)) StaxEch0021.nameOfParentAtBirth(xmlParser, personToChange.nameOfParents.mother);
				else if (startName.equals(MARITAL_STATUS)) enuum(xmlParser, personToChange, Person.PERSON.maritalStatus.maritalStatus);
				else if (startName.equals(DATE_OF_MARITAL_STATUS)) personToChange.maritalStatus.dateOfMaritalStatus = StaxEch.date(xmlParser);
				else if (startName.equals(PARTNER_SHIP_ABOLITION)) enuum(xmlParser, personToChange, Person.PERSON.cancelationReason); // Das Feld heisst in e11 wirklich anders als in e20
				else if (startName.equals(NATIONALITY)) StaxEch0011.nationality(xmlParser, personToChange.nationality);
				else if (startName.equals(RELIGION)) enuum(xmlParser, personToChange, Person.PERSON.religion);
				else if (startName.equals(SEPARATION)) enuum(xmlParser, personToChange, Person.PERSON.separation.separation);
				else if (startName.equals(DATE_OF_SEPARATION)) personToChange.separation.dateOfSeparation = StaxEch.date(xmlParser);
				else if (startName.equals(MOVE_OUT_REPORTING_DESTINATION)) moveOutReportingDestination(xmlParser, personToChange);
				else if (startName.equals(MOVE_REPORTING_ADDRESS)) moveReportingAddress(xmlParser, personToChange);
				else if (startName.equals(RESIDENCE_PERMIT)) enuum(xmlParser, personToChange.foreign, Foreign.FOREIGN.residencePermit);
				else if (startName.equals(RESIDENCE_PERMIT_TILL)) personToChange.foreign.residencePermitTill = StaxEch.date(xmlParser);
				else if (startName.equals(CONTACT)) StaxEch0011.contact(xmlParser, personToChange);
				else if (startName.equals(ORIGINAL_NAME)) personToChange.originalName = token(xmlParser);
				else if (startName.equals(ALLIANCE_PARTNERSHIP_NAME)) personToChange.alliancePartnershipName = token(xmlParser);
				else if (startName.equals(ALIAS_NAME)) personToChange.aliasName = token(xmlParser);
				else if (startName.equals(OTHER_NAME)) personToChange.otherName = token(xmlParser);
				else if (startName.equals(CALL_NAME)) personToChange.callName = token(xmlParser);
				else if (startName.equals(NAME_ON_PASSPORT)) personToChange.foreign.nameOnPassport = token(xmlParser);
				else if (StringUtils.equals(startName, LANGUAGE_OF_CORRESPONDANCE, "languageOfCorrespondence")) enuum(xmlParser, personToChange, Person.PERSON.languageOfCorrespondance);
				else if (startName.equals(PLACE_OF_BIRTH)) personToChange.placeOfBirth = StaxEch0011.birthplace(xmlParser);
				else if (startName.equals(MARITAL_DATA)) StaxEch0011.maritalData(xmlParser, personToChange);				
				else if (startName.equals(MAIN_RESIDENCE_ADDRESS)) personToChange.dwellingAddress = StaxEch0011.dwellingAddress(xmlParser);				
				else if (startName.equals(DATA_LOCK)) personToChange.dataLock = token(xmlParser);
				else if (startName.equals(PAPER_LOCK)) personToChange.paperLock = token(xmlParser);
				else if (startName.equals(CHANGE_RESIDENCE_TYPE_REPORTING_RELATIONSHIP)) changeResidenceTypeReportingMunicipality(xmlParser, personToChange);
				else if (startName.equals(EXTENSION)) extension(xmlParser, personToChange);
				else residenceChoiceOrSkip(startName, xmlParser, personToChange);
			} else if (isEndTag(event)) {
				simplePersonEvent(eventName, personIdentification, personToChange);
				return;
			} // else skip
		}
	}
	
	
	private void removeGardianRelationship() {
		for (int i = personToChange.relation.size()-1; i>= 0; i--) {
			Relation relation = personToChange.relation.get(i);
			if (relation.isCareRelation()) {
				personToChange.relation.remove(i);
			}
		}
	}

	// correctOccupationPerson etc
	private PersonIdentification simplePersonEventPerson(String eventName, XmlPullParser xmlParser) throws XmlPullParserException, IOException {
		PersonIdentification personIdentification = null;
		
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				String startName = xmlParser.getName();
				if (startName.equals(PERSON_IDENTIFICATION) || startName.equals(PERSON_IDENTIFICATION_BEFORE)) {
					personIdentification = StaxEch0044.personIdentification(xmlParser);
					personToChange = getPerson(personIdentification);
					if (StringUtils.equals(eventName, CHANGE_NAME, CHANGE_RESIDENCE_TYPE, CORRECT_NAME, CORRECT_PERSON)) {
						// Müssen gelöscht werden, damit bei fehlendem Element die Werte nicht bestehen bleiben
						personToChange.originalName = null;
						personToChange.alliancePartnershipName = null;
						personToChange.aliasName = null;
						personToChange.otherName = null;
						personToChange.callName = null;
						if (CORRECT_PERSON.equals(eventName)) {
							personToChange.dateOfDeath = null;
							personToChange.languageOfCorrespondance = null;
							personToChange.separation.clear();
						}
					}
				} else if (startName.equals(PERSON_IDENTIFICATION_AFTER)) {
					// die lokale Id darf von aussen nicht verändert werden.
					String savedLocalId = personToChange.getId();
					StaxEch0044.personIdentification(xmlParser, personToChange.personIdentification);
					personToChange.personIdentification.technicalIds.localId.personId = savedLocalId;
				}
				else if (startName.equals(OFFICIAL_NAME)) personToChange.personIdentification.officialName = token(xmlParser);
				else if (startName.equals(FIRST_NAME)) personToChange.personIdentification.firstName = token(xmlParser);
				else if (startName.equals(ORIGINAL_NAME)) personToChange.originalName = token(xmlParser);
				else if (startName.equals(ALLIANCE_PARTNERSHIP_NAME)) personToChange.alliancePartnershipName = token(xmlParser);
				else if (startName.equals(ALIAS_NAME)) personToChange.aliasName = token(xmlParser);
				else if (startName.equals(OTHER_NAME)) personToChange.otherName = token(xmlParser);
				else if (startName.equals(CALL_NAME)) personToChange.callName = token(xmlParser);
				else if (startName.equals(RELIGION)) enuum(xmlParser, personToChange, Person.PERSON.religion);
				else if (startName.equals(DATA_LOCK)) personToChange.dataLock = token(xmlParser);
				else if (startName.equals(PAPER_LOCK)) personToChange.paperLock = token(xmlParser);

				else if (startName.equals(DATE_OF_DEATH)) personToChange.dateOfDeath = StaxEch.date(xmlParser);

				else if (StringUtils.equals(startName, LANGUAGE_OF_CORRESPONDANCE, "languageOfCorrespondence")) enuum(xmlParser, personToChange, Person.PERSON.languageOfCorrespondance);

				else if (startName.equals(NAME_ON_PASSPORT)) personToChange.foreign.nameOnPassport = token(xmlParser);

				else if (startName.equals(PLACE_OF_BIRTH)) personToChange.placeOfBirth = StaxEch0011.birthplace(xmlParser);
				else if (startName.equals(MARITAL_DATA)) StaxEch0011.maritalData(xmlParser, personToChange);
				else if (startName.equals(NATIONALITY)) StaxEch0011.nationality(xmlParser, personToChange.nationality); 		
				else if (startName.equals(ANY_PERSON)) StaxEch0011.anyPerson(xmlParser, personToChange);
				else if (startName.equals(CHANGE_RESIDENCE_TYPE_ORIGIN_ADD_ON) || startName.equals(PLACE_OF_ORIGIN_ADDON)) StaxEch0021.addPlaceOfOriginAddon(xmlParser, personToChange.placeOfOrigin);
				else if (startName.equals(CONTACT)) StaxEch0011.contact(xmlParser, personToChange);
				else skip(xmlParser);
			} else if (isEndTag(event)) {
				return personIdentification;
			} // else skip
		}
	}

}
