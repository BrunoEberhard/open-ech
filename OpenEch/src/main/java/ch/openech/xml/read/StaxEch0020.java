package ch.openech.xml.read;

import static ch.openech.dm.XmlConstants.*;
import static ch.openech.xml.read.StaxEch.skip;
import static ch.openech.xml.read.StaxEch.token;

import java.io.InputStream;
import java.io.StringReader;
import java.sql.SQLException;

import javax.swing.ProgressMonitor;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ch.openech.dm.Event;
import ch.openech.dm.XmlConstants;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Relation;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.ProgressListener;
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
	// (war früher abgetrennt in eigener Klasse)
	
	public void insertPerson(Person person) {
		try {
			person.event = e;
			persistence.person().insert(person);
			lastInsertedPersonId = person.getId();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getLastInsertedId() {
		return lastInsertedPersonId;
	}

	public void simplePersonEvent(String type, PersonIdentification personIdentification, Person person) {
		if (StringUtils.equals(type, XmlConstants.DIVORCE, XmlConstants.UNDO_MARRIAGE, XmlConstants.UNDO_PARTNERSHIP)) removePartner(person);

		try {
			person.event = e;
			persistence.person().update(person);
			persistence.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
			return persistence.person().getByLocalPersonId(personIdentification.getId());
		} else if (!StringUtils.isBlank(personIdentification.vn)) {
			return persistence.person().getByVn(personIdentification.vn);
		} else {
			return persistence.person().getByName(personIdentification.officialName, personIdentification.firstName, personIdentification.dateOfBirth);
		}
	}
	
	//
	
	public void process(String xmlString) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(new StringReader(xmlString));
		
		process(xml, xmlString, null);
		xml.close();
	}

	public void process(InputStream inputStream, String eventString, ProgressListener progressListener) throws XMLStreamException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader xml = inputFactory.createXMLEventReader(inputStream);
		
		process(xml, eventString, progressListener);
		xml.close();
	}

	private void process(XMLEventReader xml, String xmlString, ProgressListener progressListener) throws XMLStreamException {
		while (xml.hasNext() && !isCanceled(progressListener)) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(DELIVERY)) {
					delivery(xmlString, xml, progressListener);
				}
				else skip(xml);
			} 
		}
	}
	
	private void delivery(String xmlString, XMLEventReader xml, ProgressListener progressListener) throws XMLStreamException {
		while (!isCanceled(progressListener)) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(DELIVERY_HEADER)) {
					skip(xml);
				} else {
					e = new Event();
					e.type = startName;
					// e.message = xmlString;
					e.time = DateUtils.getToday();
					
					if (startName.equals(BASE_DELIVERY)) baseDelivery(xml, progressListener);
					else if (startName.equals(BIRTH)) eventBirth(xml);
					else if (startName.equals(MOVE_IN)) eventMoveIn(xml);
					else if (StringUtils.equals(startName, NATURALIZE_FOREIGNER, NATURALIZE_SWISS, UNDO_CITIZEN)) eventNaturalize(startName, xml);
					else simplePersonEvent(startName, xml);
				}
			} else if (event.isEndElement()) return;
			// else skip
		}
	}
	
	private boolean isCanceled(ProgressListener progressListener) {
		return (progressListener instanceof ProgressMonitor) && ((ProgressMonitor)progressListener).isCanceled();
	}
	
	private void baseDelivery(XMLEventReader xml, ProgressListener progressListener) throws XMLStreamException {
		int numberOfMessages = 1;
		int count = 0;
		
		while (!isCanceled(progressListener)) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(MESSAGES)) {
					messages(xml);
					if (progressListener != null) progressListener.showProgress(count++, numberOfMessages);
				}
				else if (startName.equals(NUMBER_OF_MESSAGES)) numberOfMessages = StaxEch.integer(xml);
				else skip(xml);
			} else if (event.isEndElement()) return;
			// else skip
		}
	}

	public void messages(XMLEventReader xml) throws XMLStreamException {
		Person person = new Person();
		
		while(true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(BASE_DELIVERY_PERSON)) baseDeliveryPerson(xml, person);
				else if (startName.equals(NAME_OF_FATHER)) StaxEch0021.nameOfParentAtBirth(xml, person.getFather());
				else if (startName.equals(NAME_OF_MOTHER)) StaxEch0021.nameOfParentAtBirth(xml, person.getMother());
				else if (startName.equals(_RELATIONSHIP) || startName.equals(RELATIONSHIP)) StaxEch0021.relation(xml, person);
				else if (startName.equals(OCCUPATION)) person.occupation.add(StaxEch0021.occupation(xml));
				else if (startName.equals(DATA_LOCK)) person.dataLock = token(xml);
				else if (startName.equals(PAPER_LOCK)) person.paperLock = token(xml);
				else if (startName.equals(EXTENSION)) extension(xml, person);
				// TODO householdNumber
				else residenceChoiceOrSkip(startName, xml, person);
			} else if (event.isEndElement()) {
				insertPerson(person);	
				return;
			}
			// else skip
		}
	}
	
	public static void residenceChoiceOrSkip(String startName, XMLEventReader xml, Person person) throws XMLStreamException {
		if (startName.equals(HAS_MAIN_RESIDENCE)) {
			person.typeOfResidence = "1";
			StaxEch0011.mainResidenceType(xml, person);
		} else if (startName.equals(HAS_SECONDARY_RESIDENCE)) {
			person.typeOfResidence = "2";
			StaxEch0011.secondaryResidenceType(xml, person);
		} else if (startName.equals(HAS_OTHER_RESIDENCE)) {
			person.typeOfResidence = "3";
			StaxEch0011.otherResidenceType(xml, person);
		} else {
			System.out.println("Skipping: " + startName);
			skip(xml);
		}
	}

	public void extension(XMLEventReader xml, Person personToChange) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (CONTACT.equals(startName)) personToChange.contact = StaxEch0046.contact(xml);
				else if ("personExtendedInformation".equals(startName)) personToChange.personExtendedInformation = StaxEch0101.information(xml);
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	public void baseDeliveryPerson(XMLEventReader xml, Person values) throws XMLStreamException {
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(PERSON)) StaxEch0011.person(xml, values);
				else if (startName.equals(ANY_PERSON)) StaxEch0011.anyPerson(xml, values);
				else if (startName.equals(PLACE_OF_ORIGIN_ADDON)) StaxEch0021.addPlaceOfOriginAddon(xml, values.placeOfOrigin);
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}

	public void eventBirth(XMLEventReader xml) throws XMLStreamException {
		Person person = null;
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(BIRTH_PERSON)) person = newPerson(xml);
				else if (startName.equals(MOTHER)) birthParent(false, xml, person);
				else if (startName.equals(FATHER)) birthParent(true, xml, person);
				else skip(xml);
			} else if (event.isEndElement()) {
				completePlaceOfOrigins(person);
				copyResidence(person);
				insertPerson(person);
				return;
			} // else skip
		}
	}
	
	private void completePlaceOfOrigins(Person person) {
		for (PlaceOfOrigin placeOfOrigin : person.placeOfOrigin) {
			placeOfOrigin.naturalizationDate = person.getDateOfBirth();
			placeOfOrigin.reasonOfAcquisition = "1"; // Abstammung
		}
	}
	
	private void copyResidence(Person person) {
		Relation relation = person.getMother();
		if (relation.isEmpty()) relation = person.getFather();
		if (relation.isEmpty()) {
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
	public static void birthParent(boolean father, XMLEventReader xml, Person person) throws XMLStreamException {
		Relation relation = new Relation();
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(TYPE_OF_RELATIONSHIP)) relation.typeOfRelationship = token(xml);
				else if (startName.equals(PARTNER)) {
					birthPartner(xml, relation);
					relation.care = "1";
					person.relation.add(relation);
				}
				else if (startName.equals(NAME_AT_BIRTH)) StaxEch0021.nameOfParentAtBirth(xml, father ? person.getFather() : person.getMother());
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	public static void birthPartner(XMLEventReader xml, Relation relation) throws XMLStreamException {
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(PERSON_IDENTIFICATION) || //
						startName.equals(PERSON_IDENTIFICATION_PARTNER)) relation.partner = StaxEch0044.personIdentification(xml);
				else if (startName.equals(ADDRESS)) relation.address = StaxEch0010.address(xml) ;
				else skip(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	public void eventMoveIn(XMLEventReader xml) throws XMLStreamException {
		Person person = null;
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(MOVE_IN_PERSON)) person = newPerson(xml);
				else if (startName.equals(NAME_OF_FATHER)) StaxEch0021.nameOfParentAtBirth(xml, person.getFather());
				else if (startName.equals(NAME_OF_MOTHER)) StaxEch0021.nameOfParentAtBirth(xml, person.getMother());
				else if (startName.equals(OCCUPATION)) person.occupation.add(StaxEch0021.occupation(xml));
				else if (startName.equals(_RELATIONSHIP) || startName.equals(RELATIONSHIP)) StaxEch0021.relation(xml, person);
				else if (startName.equals(EXTENSION)) extension(xml, person);
				else residenceChoiceOrSkip(startName, xml, person);
			} else if (event.isEndElement()) {
				insertPerson(person);
				return;
			} // else skip
		}
	}
	
	public void moveOutReportingDestination(XMLEventReader xml, Person personToChange) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(REPORTING_MUNICIPALITY)) personToChange.residence.reportingMunicipality = StaxEch0007.municipality(xml);
				else if (startName.equals(FEDERAL_REGISTER)) personToChange.residence.reportingMunicipality = federalRegister(xml);
				else if (startName.equals(GOES_TO)) personToChange.goesTo = StaxEch0011.destination(xml);
				else if (startName.equals(DEPARTURE_DATE)) personToChange.departureDate = StaxEch.date(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	private MunicipalityIdentification federalRegister(XMLEventReader xml) throws XMLStreamException {
		MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
		municipalityIdentification.historyMunicipalityId = "-" + token(xml);
		return municipalityIdentification;
	}
	
	public void moveReportingAddress(XMLEventReader xml, Person personToChange) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(REPORTING_MUNICIPALITY)) personToChange.residence.reportingMunicipality = StaxEch0007.municipality(xml);
				else if (startName.equals(FEDERAL_REGISTER)) personToChange.residence.reportingMunicipality = federalRegister(xml);
				else if (startName.equals(DWELLING_ADDRESS)) personToChange.dwellingAddress = StaxEch0011.dwellingAddress(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	// used as moveInPerson, birthPerson, changeResidenceType
	// also used in e93
	public static Person newPerson(XMLEventReader xml) throws XMLStreamException {
		Person person = new Person();

		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String name = element.getName().getLocalPart();
				if (name.equals(PERSON_IDENTIFICATION)) StaxEch0044.personIdentification(xml, person.personIdentification);
				else if (name.equals(ORIGINAL_NAME)) person.originalName = token(xml);
				else if (name.equals(ALLIANCE_PARTNERSHIP_NAME)) person.alliancePartnershipName = token(xml);
				else if (name.equals(ALIAS_NAME)) person.aliasName = token(xml);
				else if (name.equals(OTHER_NAME)) person.otherName = token(xml);
				else if (name.equals(CALL_NAME)) person.callName = token(xml);
				else if (name.equals(PLACE_OF_BIRTH)) person.placeOfBirth = StaxEch0011.birthplace(xml);
				else if (name.equals(NATIONALITY)) StaxEch0011.nationality(xml, person.nationality);
				else if (name.equals(CONTACT)) StaxEch0011.contact(xml, person);
				else if (name.equals(RELIGION)) person.religion = token(xml);
				else if (StringUtils.equals(name, LANGUAGE_OF_CORRESPONDANCE, "languageOfCorrespondence")) person.languageOfCorrespondance = token(xml);
				else if (name.equals(MARITAL_DATA)) StaxEch0011.maritalData(xml, person);
				else if (name.equals(ANY_PERSON)) StaxEch0011.anyPerson(xml, person);
				else if (name.equals(PLACE_OF_ORIGIN_ADDON) || name.equals(CHANGE_RESIDENCE_TYPE_ORIGIN_ADD_ON)) StaxEch0021.addPlaceOfOriginAddon(xml, person.placeOfOrigin);
				else skip(xml);
			} else if (event.isEndElement()) return person;
			// else skip
		}
	}
	
	// Swiss & Foreign
	public void eventNaturalize(String eventName, XMLEventReader xml) throws XMLStreamException {
		PersonIdentification personIdentification = null;
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.endsWith("Person")) personIdentification = simplePersonEventPerson(xml);
				else if (StringUtils.equals(startName, PLACE_OF_ORIGIN)) StaxEch0011.placeOfOrigin(xml, placeOfOrigin);
				else if (startName.equals(NATURALIZATION_DATE)) placeOfOrigin.naturalizationDate = StaxEch.date(xml);
				else if (startName.equals(REASON_OF_ACQUISITION)) placeOfOrigin.reasonOfAcquisition = token(xml);
				else if (startName.equals(EXPATRIATION_DATE)) placeOfOrigin.expatriationDate = StaxEch.date(xml);
				else if (startName.equals(NATIONALITY)) StaxEch0011.nationality(xml, personToChange.nationality);
				else skip(xml);
			} else if (event.isEndElement()) {
				StaxEch0021.updatePlaceOfOrigin(personToChange.placeOfOrigin, placeOfOrigin);
				personToChange.foreign.residencePermit = null;
				personToChange.foreign.residencePermitTill = null;
				simplePersonEvent(eventName, personIdentification, personToChange);
				return;
			} // else skip
		}
	}
	
	public void changeResidenceTypeReportingMunicipality(XMLEventReader xml, Person personToChange) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(REPORTING_MUNICIPALITY)) personToChange.residence.reportingMunicipality = StaxEch0007.municipality(xml);
				else if (startName.equals(TYP_OF_RESIDENCE)) personToChange.typeOfResidence = token(xml);
				else if (startName.equals(ARRIVAL_DATE)) personToChange.arrivalDate = StaxEch.date(xml);
				else if (startName.equals(COMES_FROM)) personToChange.comesFrom = StaxEch0011.destination(xml);
				else if (startName.equals(DWELLING_ADDRESS)) personToChange.dwellingAddress = StaxEch0011.dwellingAddress(xml);
			} else if (event.isEndElement()) {
				return;
			} // else skip
		}
	}
	
	// correctOccupation etc
	private void simplePersonEvent(String eventName, XMLEventReader xml) throws XMLStreamException {
		PersonIdentification personIdentification = null;
		personToChange = null;
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (StringUtils.equals(startName, eventName + "Person", CORRECT_PERSON, //
						NATURALIZATION_FOREIGNER_PERSON, CHANGE_RESIDENCE_PERMIT_PERSON, //
						GARDIAN_MEASURE_PERSON, CHANGE_OF_OCCUPATION_PERSON, //
						CONTACT_ADDRESS_PERSON, NATURALIZATION_SWISS_PERSON, CHANGE_OF_RELIGION_PERSON, //
						CITIZENSHIP_DISCHARGE_PERSON)) {
					personIdentification = simplePersonEventPerson(xml);
					
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
//					if (CORRECT_PERSON.equals(eventName)) personToChange.dateOfDeath = null;
				}
				else if (startName.equals(OCCUPATION)) personToChange.occupation.add(StaxEch0021.occupation(xml));
				else if (startName.equals(_RELATIONSHIP) || startName.equals(RELATIONSHIP) || startName.equals(_RELATIONSHIP_TYPE) || //
						startName.equals(MARRIAGE_RELATIONSHIP) || startName.equals(PARTNERSHIP_RELATIONSHIP) || //
						startName.equals(CARE_RELATIONSHIP)) StaxEch0021.relation(xml, personToChange);
				else if (startName.equals(PLACE_OF_ORIGIN_ADDON)) StaxEch0021.addPlaceOfOriginAddon(xml, personToChange.placeOfOrigin);
				else if (StringUtils.equals(startName, PLACE_OF_ORIGIN)) {
					// Das ist bis jetzt der einzige Fall, wo ein Attribute gleich heisst, aber einen unterschiedlichen Typ hat.
					// (Eigentlich ist es in correctOrigin einfach falsch)
					if (CORRECT_ORIGIN.equals(eventName)) {
						StaxEch0021.addPlaceOfOriginAddon(xml, personToChange.placeOfOrigin);
					} else {
						personToChange.placeOfOrigin.add(StaxEch0011.placeOfOrigin(xml));
					}
				}
				else if (StringUtils.equals(startName, ORIGIN)) StaxEch0021.addPlaceOfOriginAddon(xml, personToChange.placeOfOrigin);
				else if (startName.equals(EXPATRIATION_DATE)) personToChange.placeOfOrigin.get(0).expatriationDate = StaxEch.date(xml);
				else if (startName.equals(DATE_OF_DEATH)) personToChange.dateOfDeath = StaxEch.date(xml);
				else if (startName.equals(NAME_OF_FATHER)) StaxEch0021.nameOfParentAtBirth(xml, personToChange.getFather());
				else if (startName.equals(NAME_OF_MOTHER)) StaxEch0021.nameOfParentAtBirth(xml, personToChange.getMother());
				else if (startName.equals(MARITAL_STATUS)) personToChange.maritalStatus.maritalStatus = token(xml);
				else if (startName.equals(DATE_OF_MARITAL_STATUS)) personToChange.maritalStatus.dateOfMaritalStatus = StaxEch.date(xml);
				else if (startName.equals(PARTNER_SHIP_ABOLITION)) personToChange.cancelationReason = token(xml); // Das Feld heisst in e11 wirklich anders als in e20
				else if (startName.equals(NATIONALITY)) StaxEch0011.nationality(xml, personToChange.nationality);
				else if (startName.equals(RELIGION)) personToChange.religion = token(xml);
				else if (startName.equals(SEPARATION)) personToChange.separation.separation = token(xml);
				else if (startName.equals(DATE_OF_SEPARATION)) personToChange.separation.dateOfSeparation = StaxEch.date(xml);
				else if (startName.equals(MOVE_OUT_REPORTING_DESTINATION)) moveOutReportingDestination(xml, personToChange);
				else if (startName.equals(MOVE_REPORTING_ADDRESS)) moveReportingAddress(xml, personToChange);
				else if (startName.equals(RESIDENCE_PERMIT)) personToChange.foreign.residencePermit = token(xml);
				else if (startName.equals(RESIDENCE_PERMIT_TILL)) personToChange.foreign.residencePermitTill = StaxEch.date(xml);
				else if (startName.equals(CONTACT)) StaxEch0011.contact(xml, personToChange);
				else if (startName.equals(ORIGINAL_NAME)) personToChange.originalName = token(xml);
				else if (startName.equals(ALLIANCE_PARTNERSHIP_NAME)) personToChange.alliancePartnershipName = token(xml);
				else if (startName.equals(ALIAS_NAME)) personToChange.aliasName = token(xml);
				else if (startName.equals(OTHER_NAME)) personToChange.otherName = token(xml);
				else if (startName.equals(CALL_NAME)) personToChange.callName = token(xml);
				else if (startName.equals(NAME_ON_PASSPORT)) personToChange.foreign.nameOnPassport = token(xml);
				else if (StringUtils.equals(startName, LANGUAGE_OF_CORRESPONDANCE, "languageOfCorrespondence")) personToChange.languageOfCorrespondance = token(xml);
				else if (startName.equals(PLACE_OF_BIRTH)) personToChange.placeOfBirth = StaxEch0011.birthplace(xml);
				else if (startName.equals(MARITAL_DATA)) StaxEch0011.maritalData(xml, personToChange);				
				else if (startName.equals(MAIN_RESIDENCE_ADDRESS)) personToChange.dwellingAddress = StaxEch0011.dwellingAddress(xml);				
				else if (startName.equals(DATA_LOCK)) personToChange.dataLock = token(xml);
				else if (startName.equals(PAPER_LOCK)) personToChange.paperLock = token(xml);
				else if (startName.equals(CHANGE_RESIDENCE_TYPE_REPORTING_RELATIONSHIP)) changeResidenceTypeReportingMunicipality(xml, personToChange);
				else if (startName.equals(EXTENSION)) extension(xml, personToChange);
				else residenceChoiceOrSkip(startName, xml, personToChange);
			} else if (event.isEndElement()) {
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
	private PersonIdentification simplePersonEventPerson(XMLEventReader xml) throws XMLStreamException {
		PersonIdentification personIdentification = null;
		
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				String startName = startElement.getName().getLocalPart();
				if (startName.equals(PERSON_IDENTIFICATION) || startName.equals(PERSON_IDENTIFICATION_BEFORE)) {
					personIdentification = StaxEch0044.personIdentification(xml);
					personToChange = getPerson(personIdentification);
					personToChange.dateOfDeath = null;
					personToChange.callName = null;
					personToChange.aliasName = null;
					personToChange.originalName = null;
					personToChange.otherName = null;
					personToChange.alliancePartnershipName = null;
				} else if (startName.equals(PERSON_IDENTIFICATION_AFTER)) {
					// die lokale Id darf von aussen nicht verändert werden.
					String savedLocalId = personToChange.getId();
					StaxEch0044.personIdentification(xml, personToChange.personIdentification);
					personToChange.personIdentification.technicalIds.localId.personId = savedLocalId;
				}
				else if (startName.equals(OFFICIAL_NAME)) personToChange.personIdentification.officialName = token(xml);
				else if (startName.equals(FIRST_NAME)) personToChange.personIdentification.firstName = token(xml);
				else if (startName.equals(ORIGINAL_NAME)) personToChange.originalName = token(xml);
				else if (startName.equals(ALLIANCE_PARTNERSHIP_NAME)) personToChange.alliancePartnershipName = token(xml);
				else if (startName.equals(ALIAS_NAME)) personToChange.aliasName = token(xml);
				else if (startName.equals(OTHER_NAME)) personToChange.otherName = token(xml);
				else if (startName.equals(CALL_NAME)) personToChange.callName = token(xml);
				else if (startName.equals(RELIGION)) personToChange.religion = token(xml);
				else if (startName.equals(DATA_LOCK)) personToChange.dataLock = token(xml);
				else if (startName.equals(PAPER_LOCK)) personToChange.paperLock = token(xml);

				else if (startName.equals(DATE_OF_DEATH)) personToChange.dateOfDeath = StaxEch.date(xml);

				else if (StringUtils.equals(startName, LANGUAGE_OF_CORRESPONDANCE, "languageOfCorrespondence")) personToChange.languageOfCorrespondance = token(xml);

				else if (startName.equals(NAME_ON_PASSPORT)) personToChange.foreign.nameOnPassport = token(xml);

				else if (startName.equals(PLACE_OF_BIRTH)) personToChange.placeOfBirth = StaxEch0011.birthplace(xml);
				else if (startName.equals(MARITAL_DATA)) StaxEch0011.maritalData(xml, personToChange);
				else if (startName.equals(NATIONALITY)) StaxEch0011.nationality(xml, personToChange.nationality); 		
				else if (startName.equals(ANY_PERSON)) StaxEch0011.anyPerson(xml, personToChange);
				else if (startName.equals(CHANGE_RESIDENCE_TYPE_ORIGIN_ADD_ON) || startName.equals(PLACE_OF_ORIGIN_ADDON)) StaxEch0021.addPlaceOfOriginAddon(xml, personToChange.placeOfOrigin);
				else if (startName.equals(CONTACT)) StaxEch0011.contact(xml, personToChange);
				else skip(xml);
			} else if (event.isEndElement()) {
				return personIdentification;
			} // else skip
		}
	}

}
