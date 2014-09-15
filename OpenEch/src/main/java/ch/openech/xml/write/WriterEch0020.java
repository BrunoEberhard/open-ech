package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;

import java.util.List;

import org.threeten.bp.LocalDate;

import ch.openech.model.common.DwellingAddress;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.Place;
import ch.openech.model.common.Swiss;
import ch.openech.model.person.Foreign;
import ch.openech.model.person.NameOfParent;
import ch.openech.model.person.Nationality;
import ch.openech.model.person.Occupation;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.model.person.Relation;
import ch.openech.model.person.SecondaryResidence;
import ch.openech.model.person.types.MaritalStatus;
import ch.openech.model.person.types.PartnerShipAbolition;
import ch.openech.model.person.types.Religion;
import ch.openech.model.person.types.Separation;
import ch.openech.model.types.TypeOfResidence;

public class WriterEch0020 extends DeliveryWriter {

	public final String URI;
	public final WriterEch0007 ech7;
	public final WriterEch0010 ech10;
	public final WriterEch0011 ech11;
	public final WriterEch0021 ech21;
	public final WriterEch0044 ech44;
	public final WriterOpenEch openEch;
	
	public WriterEch0020(EchSchema context) {
		super(context);
		
		URI = context.getNamespaceURI(20);
		ech7 = new WriterEch0007(context);
		ech10 = new WriterEch0010(context);
		ech11 = new WriterEch0011(context);
		ech21 = new WriterEch0021(context);
		ech44 = new WriterEch0044(context);
		openEch = new WriterOpenEch(context);
	}
	
	@Override
	protected int getSchemaNumber() {
		return 20;
	}
	
	@Override
	protected String getNamespaceURI() {
		return URI;
	}
	
	// Elemente für 0
	
	public WriterElement baseDelivery(WriterElement delivery, Integer messageCount) throws Exception {
		WriterElement baseDelivery = delivery.create(URI, BASE_DELIVERY);
		baseDelivery.text(NUMBER_OF_MESSAGES, messageCount.toString());
		return baseDelivery;
	}
	
	public void addParentNames(WriterElement element, Person person) throws Exception {
	   	ech21.nameOfParentAtBirth(element, NAME_OF_FATHER, person.nameOfParents.father.firstName, person.nameOfParents.father.officialName);
	   	ech21.nameOfParentAtBirth(element, NAME_OF_MOTHER, person.nameOfParents.mother.firstName, person.nameOfParents.mother.officialName);
	}
	
	public void eventBaseDelivery(WriterElement baseDelivery, Person values) throws Exception {
		// Der Typ heisst "eventBaseDelivery", das Element heisst MESSAGES
		WriterElement message = baseDelivery.create(URI, MESSAGES);
	
		baseDeliveryPerson(message, values);
		addParentNames(message, values);

		for (Relation relation : values.relation) ech21.relation(message, relation);
    	for (Occupation occupation : values.occupation) ech21.occupation(message, occupation);
    	// TODO householdNumber
    	message.values(values, DATA_LOCK, PAPER_LOCK);
        
        ech11.residence(message, values);
        openEch.openEchPersonExtension(message, EXTENSION, values);
	}

	public void eventBaseDelivery(WriterElement baseDelivery, List<Person> persons) throws Exception {
		for (Person person : persons) {
			eventBaseDelivery(baseDelivery, person);
		}
	}
	
	private void baseDeliveryPerson(WriterElement eventBaseDelivery, Person person) throws Exception {
		WriterElement baseDeliveryPerson = eventBaseDelivery.create(URI, BASE_DELIVERY_PERSON);
		ech11.person(baseDeliveryPerson, person);
		ech11.anyPerson(baseDeliveryPerson, person);
		for (PlaceOfOrigin placeOfOrigin : person.placeOfOrigin) ech21.placeOfOriginAddon(baseDeliveryPerson, PLACE_OF_ORIGIN_ADDON, placeOfOrigin);
	}
	
	// Elemente für alle Events
	
	private WriterElement simplePersonEvent(String eventName, PersonIdentification personIdentification) throws Exception {
		return simplePersonEvent(delivery(), eventName, personIdentification);
	}
	
	private WriterElement simplePersonEvent(String eventName, Person person) throws Exception {
		return simplePersonEvent(delivery(), eventName, person.personIdentification());
	}
	
	private WriterElement simplePersonEvent(WriterElement delivery, String eventName, PersonIdentification personIdentification) throws Exception {
		WriterElement event = delivery.create(URI, eventName);
		
		// Das Schema ist (fast) immer das gleiche:
		// death -> deathPerson
		// missing -> missingPerson
		// etc.
		
		String personName = eventName + "Person";
		if ("renewPermitPerson".equals(personName)) personName = CHANGE_RESIDENCE_PERMIT_PERSON;
		if ("naturalizeForeignerPerson".equals(personName)) personName = NATURALIZATION_FOREIGNER_PERSON;
		if ("changeGardianPerson".equals(personName)) personName = GARDIAN_MEASURE_PERSON;
		if ("changeOccupationPerson".equals(personName)) personName = CHANGE_OF_OCCUPATION_PERSON;
		if ("contactPerson".equals(personName)) personName = CONTACT_ADDRESS_PERSON;
		if ("naturalizeSwissPerson".equals(personName)) personName = NATURALIZATION_SWISS_PERSON;
		if ("changeReligionPerson".equals(personName)) personName = CHANGE_OF_RELIGION_PERSON;
		if ("undoCitizenPerson".equals(personName)) personName = CITIZENSHIP_DISCHARGE_PERSON;
		
		WriterElement person = event.create(URI, personName);
		ech44.personIdentification(person, personIdentification);

        return event;
	}

	public static void addVariousNames(WriterElement element, Person person) throws Exception {
		element.values(person, ORIGINAL_NAME, ALLIANCE_PARTNERSHIP_NAME, ALIAS_NAME, OTHER_NAME, CALL_NAME);
	}
	
	// Elemente für 1, Geburt
	
	private void birthPerson(WriterElement event, Person person) throws Exception {
		WriterElement moveInPerson = event.create(URI, BIRTH_PERSON);
		ech44.personIdentification(moveInPerson, person.personIdentification());
		
        moveInPerson.values(person, CALL_NAME);
        if (context.birthPlaceMustNotBeUnknown()) {
    		ech11.placeOfBirth(moveInPerson, person, URI);
        } else {
    		ech11.placeOfBirth(moveInPerson, person);
        }
        ech11.nationality(moveInPerson, person.nationality);
        moveInPerson.values(person, RELIGION, LANGUAGE_OF_CORRESPONDANCE);
        ech11.anyPerson(moveInPerson, person);
	}
	
	private void birthParent(boolean father, WriterElement event, Relation parentRelation, NameOfParent nameAtBirth) throws Exception {
		if (parentRelation != null) {
			WriterElement element = event.create(URI, father ? FATHER : MOTHER);
			if (parentRelation != null) {
				element.values(parentRelation, TYPE_OF_RELATIONSHIP);
				partner(element, parentRelation);
			}
			ech21.nameOfParentAtBirth(element, NAME_AT_BIRTH, nameAtBirth.firstName, nameAtBirth.officialName);
		}
	}

	private void partner(WriterElement parentElement, Relation relation) throws Exception {
		if (relation != null) {
			WriterElement element = parentElement.create(URI, PARTNER);
			if (relation.partner != null) {
				ech44.personIdentification(element, relation.partner);
			}
			if (relation.address != null) {
				ech10.address(element, ADDRESS, relation.address);
			}
		}
	}

	// Elemente für 4, Eheschliessung
	
	private void marriageRelationship(WriterElement event, Relation relation) throws Exception {
		if (relation != null) {
			WriterElement element = event.create(URI, MARRIAGE_RELATIONSHIP);
			element.text(TYPE_OF_RELATIONSHIP, "1");
			partner(element, relation);
		}
	}
	
	// Elemente für 12, Einbürgerung Ausländer
	
	private void swissNationality(WriterElement event, String tagName) throws Exception {
		WriterElement nationality = event.create(URI, tagName);
		nationality.text(NATIONALITY_STATUS, "2");
		WriterElement country = nationality.create(URI, COUNTRY);
		country.text(COUNTRY_ID, Swiss.SWISS_COUNTRY_ID);
		country.text(COUNTRY_ID_I_S_O2, Swiss.SWISS_COUNTRY_ISO2);
		country.text(COUNTRY_NAME_SHORT, Swiss.SWISS_COUNTRY_NAME_SHORT);
	}

	// Elemente für 25, Gardian und 39, Care
	
	private void relationship(WriterElement event, String tagName, Relation relation) throws Exception {
		if (relation != null) {
			WriterElement element = event.create(URI, tagName);
			boolean gardian = relation.typeOfRelationship.isCare();
			element.text(TYPE_OF_RELATIONSHIP, relation.typeOfRelationship);
			if (!gardian) element.text(CARE, relation.care);
			partner(element, relation);
			if (relation.basedOnLaw != null) {
				element.text(BASED_ON_LAW, relation.basedOnLaw);
			}
			if (context.basedOnLawAddOn()) {
				element.textIfSet(BASED_ON_LAW_ADD_ON, relation.basedOnLawAddOn);
			}
			if (gardian && context.gardianMeasureRelationshipHasCare()) {
				element.values(relation, CARE);
			}
		}
	}
	
	// Elemente für 36, Partnerschaft
	
	private void partnershipRelationship(WriterElement event, Relation relation) throws Exception {
		if (relation != null) {
			// Version dieser elementName ist wahrscheinlich falsch (aber so im Schema)
			WriterElement element = event.create(URI, PARTNERSHIP_RELATIONSHIP);
			element.text(PARTNERSHIP_TYPE_OF_RELATIONSHIP_TYPE, "2");
			partner(element, relation);
		}
	}
	
	///////////////////////////////////
	
	// code 0
	public String person(Person person) throws Exception {
        WriterElement delivery = delivery();
        WriterElement baseDelivery = baseDelivery(delivery, 1);
        eventBaseDelivery(baseDelivery, person);

        return result();
	}
	
	public String persons(List<Person> persons) throws Exception {
        WriterElement delivery = delivery();
        WriterElement baseDelivery = baseDelivery(delivery, persons.size());
        eventBaseDelivery(baseDelivery, persons);

        return result();
	}
	
	// code 1
	public String birth(Person person) throws Exception {
        WriterElement event = delivery().create(URI, BIRTH);
        birthPerson(event, person);
        birthParent(false, event, person.getMother(), person.nameOfParents.mother);
        birthParent(true, event, person.getFather(), person.nameOfParents.father);
        return result();
	}
	
	// code 2
	public String death(PersonIdentification personIdentification, LocalDate date) throws Exception {
        WriterElement event = simplePersonEvent(DEATH, personIdentification);
        event.text(DATE_OF_DEATH, date);
        return result();
	}
	
	// code 3
	public String missing(PersonIdentification personIdentification, LocalDate date) throws Exception {
        WriterElement event = simplePersonEvent(MISSING, personIdentification);
        event.text(DATE_OF_DEATH, date);
        return result();
	}
	
	// code 4
	public String marriage(PersonIdentification personIdentification, Relation relation, LocalDate date) throws Exception {
        WriterElement event = simplePersonEvent(MARRIAGE, personIdentification);
        event.text(MARITAL_STATUS, MaritalStatus.verheiratet);
        event.text(DATE_OF_MARITAL_STATUS, date);
        marriageRelationship(event, relation);
        return result();
	}
	
	// code 6
	public String separation(PersonIdentification personIdentification, Separation separation, LocalDate date) throws Exception {
        WriterElement event = simplePersonEvent(SEPARATION, personIdentification);
        event.text(SEPARATION, separation);
        event.text(DATE_OF_SEPARATION, date);
        return result();
	}

	// code 7
	public String undoSeparation(PersonIdentification personIdentification) throws Exception {
        simplePersonEvent(UNDO_SEPARATION, personIdentification);
        return result();
	}

	// code 8
	public String divorce(PersonIdentification personIdentification, LocalDate date) throws Exception {
        WriterElement event = simplePersonEvent(DIVORCE, personIdentification);
        event.text(MARITAL_STATUS, MaritalStatus.geschieden);
        event.text(DATE_OF_MARITAL_STATUS, date);
        return result();
	}

	// code 10
	public String maritalStatusPartner(PersonIdentification personIdentification, MaritalStatus maritalStatus, LocalDate dateOfMaritalStatus, PartnerShipAbolition partnerShipAbolition) throws Exception {
        WriterElement event = simplePersonEvent(MARITAL_STATUS_PARTNER, personIdentification);
        event.text(MARITAL_STATUS, maritalStatus);
        event.text(DATE_OF_MARITAL_STATUS, dateOfMaritalStatus);
        event.text(PARTNER_SHIP_ABOLITION, partnerShipAbolition);
        return result();
	}
	
	// code 11
	public String undoMarriage(PersonIdentification personIdentification, LocalDate date) throws Exception {
        WriterElement event = simplePersonEvent(UNDO_MARRIAGE, personIdentification);
        event.text(MARITAL_STATUS, MaritalStatus.ungueltig);
        event.text(DATE_OF_MARITAL_STATUS, date);
        return result();
	}
	
	// code 12
	public String naturalizeForeigner(PersonIdentification personIdentification, PlaceOfOrigin placeOfOrigin) throws Exception {
        WriterElement event = simplePersonEvent(NATURALIZE_FOREIGNER, personIdentification);
        ech11.placeOfOrigin(event, PLACE_OF_ORIGIN, placeOfOrigin);
        event.values(placeOfOrigin, NATURALIZATION_DATE, REASON_OF_ACQUISITION);
        swissNationality(event, NATIONALITY);
        return result();
	}

	// code 13
	public String naturalizeSwiss(PersonIdentification personIdentification, PlaceOfOrigin placeOfOrigin) throws Exception {
        WriterElement event = simplePersonEvent(NATURALIZE_SWISS, personIdentification);
        ech11.placeOfOrigin(event, PLACE_OF_ORIGIN, placeOfOrigin);
        event.values(placeOfOrigin, NATURALIZATION_DATE, REASON_OF_ACQUISITION);
        return result();
	}
	
	// code 14
	public String undoCitizen(PersonIdentification personIdentification, PlaceOfOrigin placeOfOrigin, LocalDate expatriationDate) throws Exception {
        WriterElement event = simplePersonEvent(UNDO_CITIZEN, personIdentification);
        ech11.placeOfOrigin(event, PLACE_OF_ORIGIN, placeOfOrigin);
        event.text(EXPATRIATION_DATE, expatriationDate);
        return result();
	}
	
	// code 15
	public String undoSwiss(Person person) throws Exception {
        WriterElement event = simplePersonEvent(UNDO_SWISS, person);
        ech11.nationality(event, person.nationality);
        event.values(person, RESIDENCE_PERMIT, NAME_ON_PASSPORT);
        return result();
	}
	
	// code 16
	public String changeResidencePermit(Person person) throws Exception {
        WriterElement event = simplePersonEvent(CHANGE_RESIDENCE_PERMIT, person);
        for (Occupation occupation : person.occupation) ech21.occupation(event, occupation);
        event.values(person, RESIDENCE_PERMIT, RESIDENCE_PERMIT_TILL);
        return result();
	}
	
	// code 17
	public String changeNationality(PersonIdentification personIdentification, Nationality nationality) throws Exception {
        WriterElement event = simplePersonEvent(CHANGE_NATIONALITY, personIdentification);
        ech11.nationality(event, nationality);
        return result();
	}
	
	// code 18
	public String moveIn(Person person) throws Exception {
        WriterElement event = delivery().create(URI, MOVE_IN);
        moveInPerson(event, person);
        addParentNames(event, person);
        for (Occupation occupation : person.occupation) ech21.occupation(event, occupation);
        for (Relation relation: person.relation) ech21.relation(event, relation);

        if (person.typeOfResidence == TypeOfResidence.hasMainResidence) hasMainResidence(event, person);
		else if (person.typeOfResidence == TypeOfResidence.hasSecondaryResidence) hasSecondaryResidence(event, person);
		else if (person.typeOfResidence == TypeOfResidence.hasOtherResidence) hasOtherResidence(event, person);
     
        openEch.openEchPersonExtension(event, EXTENSION, person);
        return result();
	}
	
	private void moveInPerson(WriterElement event, Person person) throws Exception {
		moveInPerson(event, URI, MOVE_IN_PERSON, person);
	}
	
	public void moveInPerson(WriterElement event, String uri, String tagName, Person person) throws Exception {
		// wird auch von e93 verwendet
		WriterElement moveInPerson = event.create(uri, tagName);
		ech44.personIdentification(moveInPerson, PERSON_IDENTIFICATION, person.personIdentification());
		
		// Das entspricht fast den coredata in ech11, aber nicht ganz
		addVariousNames(moveInPerson, person);
        ech11.placeOfBirth(moveInPerson, person);
        ech11.nationality(moveInPerson, person.nationality);
        ech11.contact(moveInPerson, URI, person);
        moveInPerson.values(person, RELIGION, LANGUAGE_OF_CORRESPONDANCE);
        ech11.maritalData(moveInPerson, person);
        ech11.anyPerson(moveInPerson, person);
        for (PlaceOfOrigin placeOfOrigin : person.placeOfOrigin) ech21.placeOfOriginAddon(moveInPerson, PLACE_OF_ORIGIN_ADDON, placeOfOrigin);
	}
	
	private void hasMainResidence(WriterElement parent, Person values) throws Exception {
		WriterElement element = parent.create(URI, HAS_MAIN_RESIDENCE);
		reportingMunicipality(element, MAIN_RESIDENCE, values.residence.reportingMunicipality, values);
		if (values.residence.secondary != null) {
			for (SecondaryResidence residence : values.residence.secondary) {
				ech7.municipality(element, SECONDARY_RESIDENCE, residence);
			}
		}
	}
	
	private void hasSecondaryResidence(WriterElement parent, Person values) throws Exception {
		WriterElement element = parent.create(URI, HAS_SECONDARY_RESIDENCE);
		reportingMunicipality(element, SECONDARY_RESIDENCE, values.residence.secondary.get(0), values);
		if (values.residence.reportingMunicipality != null) {
			ech7.municipality(element, MAIN_RESIDENCE, values.residence.reportingMunicipality);
		}
	}
	
	private void hasOtherResidence(WriterElement parent, Person values) throws Exception {
		WriterElement element = parent.create(URI, HAS_OTHER_RESIDENCE);
		reportingMunicipality(element, SECONDARY_RESIDENCE, values.residence.secondary.get(0), values);
	}

	private void reportingMunicipality(WriterElement parent, String tagName, SecondaryResidence secondaryResidence, Person values) throws Exception {
		reportingMunicipality(parent, tagName, secondaryResidence.municipalityIdentification, values);
	}
	
	private void reportingMunicipality(WriterElement parent, String tagName, MunicipalityIdentification reportingMunicipality, Person values) throws Exception {
		WriterElement element = parent.create(URI, tagName);
		if (reportingMunicipality != null) {
			if (reportingMunicipality.isFederalRegister()) {
				element.text(FEDERAL_REGISTER, reportingMunicipality.getFederalRegister());
			} else {
				ech7.municipality(element, REPORTING_MUNICIPALITY, reportingMunicipality);
			}
		}
		element.values(values, ARRIVAL_DATE);
		ech11.destination(element, COMES_FROM, values.comesFrom, true);
		ech11.dwellingAddress(element, values.dwellingAddress);
	}
	
	// code 19
	public String moveOut(PersonIdentification personIdentification, MunicipalityIdentification reportingMunicipality, Place goesTo, LocalDate departureDate) throws Exception {
        WriterElement event = simplePersonEvent(MOVE_OUT, personIdentification);
        moveOutReportingDestination(event, reportingMunicipality, goesTo, departureDate);
        return result();
	}

	private void moveOutReportingDestination(WriterElement parent, MunicipalityIdentification reportingMunicipality, Place goesTo, LocalDate departureDate) throws Exception {
        WriterElement element = parent.create(URI, MOVE_OUT_REPORTING_DESTINATION);
        if (reportingMunicipality.isFederalRegister()) {
        	element.text(FEDERAL_REGISTER, reportingMunicipality.getFederalRegister());
        } else {
            ech7.municipality(element, REPORTING_MUNICIPALITY, reportingMunicipality);
        }
        ech11.destination(element, GOES_TO, goesTo, true);
        element.text(DEPARTURE_DATE, departureDate);
	}
	
	// code 20
	public String move(PersonIdentification personIdentification, MunicipalityIdentification reportingMunicipality, DwellingAddress address) throws Exception {
		WriterElement event = simplePersonEvent(MOVE, personIdentification);
        moveReportingAddress(event, reportingMunicipality, address);
        return result();
	}
	
	private void moveReportingAddress(WriterElement parent, MunicipalityIdentification reportingMunicipality, DwellingAddress address) throws Exception {
        WriterElement element = parent.create(URI, MOVE_REPORTING_ADDRESS);
        if (reportingMunicipality.isFederalRegister()) {
        	element.text(FEDERAL_REGISTER, reportingMunicipality.getFederalRegister());
        } else {
            ech7.municipality(element, REPORTING_MUNICIPALITY, reportingMunicipality);
        }
        // die e20 dwellingAddress unterscheidet sich nur gerade darin von der in e11
        // dass in e20 das movingDate obligatorisch ist
        ech11.dwellingAddress(element, URI, DWELLING_ADDRESS, address);
	}
	
	// code 21
	public String contact(Person person) throws Exception {
		// Im Schema heisst die geänderte Person leider contactAddressPerson (und nicht contactPerson)
		// das wird in simplePersonEvent(..) korrigiert.
		WriterElement event = simplePersonEvent(CONTACT, person);
		ech11.contact(event, URI, person, context.contactHasValidTill());
        return result();
	}
	
	// code 22
	public String addressLock(Person person) throws Exception {
		WriterElement event = simplePersonEvent(ADDRESS_LOCK, person);
		event.values(person, DATA_LOCK); // yep, dataLock, nicht addressLock 
        return result();
	}

	// code 23
	public String changeResidenceType(PersonIdentification personIdentification, Person person) throws Exception {
        WriterElement event = delivery().create(URI, CHANGE_RESIDENCE_TYPE);
        changeResidenceTypePerson(event, personIdentification, person);
        changeResidenceTypeReportingMunicipality(event, person);
        addParentNames(event, person);
        // Version der Name RelationshipType im Schema 20 sollte wohl eher relation heissen
        for (Relation relation: person.relation) ech21.relation(event, _RELATIONSHIP_TYPE, relation);
        for (Occupation occupation : person.occupation) ech21.occupation(event, occupation);
        
        return result();
	}
	
	private void changeResidenceTypePerson(WriterElement event, PersonIdentification personIdentification, Person person) throws Exception {
		WriterElement changeResidenceTypePerson = event.create(URI, CHANGE_RESIDENCE_TYPE_PERSON);
		ech44.personIdentification(changeResidenceTypePerson, personIdentification);
		
		addVariousNames(changeResidenceTypePerson, person);
        ech11.placeOfBirth(changeResidenceTypePerson, person);
        ech11.maritalData(changeResidenceTypePerson, person);
        ech11.nationality(changeResidenceTypePerson, person.nationality);
        ech11.anyPerson(changeResidenceTypePerson, person);
        for (PlaceOfOrigin placeOfOrigin : person.placeOfOrigin) ech21.placeOfOriginAddon(changeResidenceTypePerson, CHANGE_RESIDENCE_TYPE_ORIGIN_ADD_ON, placeOfOrigin);
        ech11.contact(changeResidenceTypePerson, URI, person);
        changeResidenceTypePerson.values(person, RELIGION, LANGUAGE_OF_CORRESPONDANCE);
	}

	private void changeResidenceTypeReportingMunicipality(WriterElement event, Person person) throws Exception {
		// Der Name des Elements ist hier wirklich anders als der des Typs
		WriterElement element = event.create(URI, CHANGE_RESIDENCE_TYPE_REPORTING_RELATIONSHIP);
		ech7.municipality(element, REPORTING_MUNICIPALITY, person.residence.reportingMunicipality);
		// VERSION: Ich nehm an das typOfResidence wird/wurde noch korrigiert in kommenden Schemas
		element.text(TYP_OF_RESIDENCE, "1");
		element.values(person, ARRIVAL_DATE);
		ech11.destination(element, COMES_FROM, person.comesFrom, true);
		ech11.dwellingAddress(element, person.dwellingAddress);
	}
	
	// code 25
	public String gardianMeasure(PersonIdentification personIdentification, Relation relation) throws Exception {
        WriterElement event = simplePersonEvent(GARDIAN_MEASURE, personIdentification);
        relationship(event, RELATIONSHIP, relation);
        return result();
	}

	// code 26
	public String undoGardian(PersonIdentification personIdentification) throws Exception {
        simplePersonEvent(UNDO_GARDIAN, personIdentification);
        return result();
	}
	
	// code 29
	public String changeName(PersonIdentification personIdentification, Person changedPerson) throws Exception {
		return changeName(personIdentification, changedPerson, false);
	}
	
	public String changeName(PersonIdentification personIdentification, Person changedPerson, boolean changeNameWithParents) throws Exception {
		// simpleEvent wird nicht verwendet, der PersonType ist anders als gewöhnlich
		WriterElement event = delivery().create(URI, CHANGE_NAME);
		WriterElement person = event.create(URI, CHANGE_NAME_PERSON);
		ech44.personIdentification(person, PERSON_IDENTIFICATION, personIdentification);
        person.values(changedPerson, OFFICIAL_NAME, FIRST_NAME);
    	addVariousNames(person, changedPerson);
    	person.values(changedPerson, NAME_ON_PASSPORT);
        if (changeNameWithParents) {
	    	ech21.nameOfParentAtBirth(event, NAME_OF_FATHER, changedPerson.nameOfParents.father.firstName, changedPerson.nameOfParents.father.officialName);
	    	ech21.nameOfParentAtBirth(event, NAME_OF_MOTHER, changedPerson.nameOfParents.mother.firstName, changedPerson.nameOfParents.mother.officialName);
        }
        return result();
	}

	// code 30
	public String changeOccupation(PersonIdentification person, Occupation occupation) throws Exception {
        WriterElement event = simplePersonEvent(CHANGE_OCCUPATION, person);
        ech21.occupation(event, occupation);
        return result();
	}
	
	// code 31
	public String changeReligion(PersonIdentification personIdentification, Religion religion) throws Exception {
        WriterElement event = simplePersonEvent(CHANGE_RELIGION, personIdentification);
        event.text(RELIGION, religion);
        return result();
	}
	
	// code 34
	public String undoMissing(PersonIdentification personIdentification) throws Exception {
        simplePersonEvent(UNDO_MISSING, personIdentification);
        return result();
	}
	
	// code 35
	public String renewPermit(PersonIdentification personIdentification, Person person) throws Exception {
        WriterElement event = simplePersonEvent(RENEW_PERMIT, personIdentification);
        // Die Reihenfolge der WriterElement ist hier anders als bei changePermit, occupation kommen am Schluss
        event.values(person.foreign, RESIDENCE_PERMIT, RESIDENCE_PERMIT_TILL); // residencePermitTill bei Version 1.0 noch nicht 
        for (Occupation o : person.occupation) ech21.occupation(event, o);
        return result();
	}
	
	// code 36
	public String partnership(PersonIdentification personIdentification, Relation relation, LocalDate date) throws Exception {
        WriterElement event = simplePersonEvent(PARTNERSHIP, personIdentification);
        event.text(MARITAL_STATUS, MaritalStatus.partnerschaft);
        event.text(DATE_OF_MARITAL_STATUS, date);
        partnershipRelationship(event, relation);
        return result();
	}
	
	// code 37
	public String undoPartnership(PersonIdentification personIdentification, LocalDate date, PartnerShipAbolition partnerShipAbolition) throws Exception {
        WriterElement event = simplePersonEvent(UNDO_PARTNERSHIP, personIdentification);
        event.text(MARITAL_STATUS, MaritalStatus.aufgeloeste_partnerschaft);
        event.text(DATE_OF_MARITAL_STATUS, date);
        event.text(PARTNER_SHIP_ABOLITION, partnerShipAbolition);
        return result();
	}
	
	// code 28
	public String paperLock(Person person) throws Exception {
		WriterElement event = simplePersonEvent(PAPER_LOCK, person);
		event.values(person, PAPER_LOCK);
        return result();
	}
	
	// code 39
	public String care(PersonIdentification personIdentification, Relation relation) throws Exception {
        WriterElement event = simplePersonEvent(CARE, personIdentification);
        relationship(event, CARE_RELATIONSHIP, relation);
        return result();
	}
	
	// code 41
	public String correctPerson(PersonIdentification personIdentification, Person person) throws Exception {
		WriterElement event = delivery().create(URI, CORRECT_PERSON);
		correctPersonPerson(event, personIdentification, person);
        openEch.openEchPersonExtension(event, EXTENSION, person);
		return result();
	}
	
	private void correctPersonPerson(WriterElement event, PersonIdentification personIdentification, Person person) throws Exception {
		// Version müsste wohl correctPersonPerson heissen, so ists etwas komisch, da damit 2 verschachtelte WriterElement gleich heissen
		WriterElement correctPerson = event.create(URI, CORRECT_PERSON);
		ech44.personIdentification(correctPerson, PERSON_IDENTIFICATION_BEFORE, personIdentification);
		ech44.personIdentification(correctPerson, PERSON_IDENTIFICATION_AFTER, person.personIdentification());
		
		addVariousNames(correctPerson, person);
        ech11.placeOfBirth(correctPerson, person);
        correctPerson.values(person, DATE_OF_DEATH);
        ech11.maritalData(correctPerson, person);
        ech11.nationality(correctPerson, person.nationality);
        ech11.contact(correctPerson, URI, person);
        correctPerson.values(person, RELIGION, LANGUAGE_OF_CORRESPONDANCE);
        ech11.anyPerson(correctPerson, person);
        for (PlaceOfOrigin placeOfOrigin : person.placeOfOrigin) ech21.placeOfOriginAddon(correctPerson, PLACE_OF_ORIGIN_ADDON, placeOfOrigin);
	}
	
	// code 42
	public String correctReporting(Person person) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_REPORTING, person);
		ech11.residence(event, person);
		return result();
	}
	
	// code 43
	public String correctAddress(Person person) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_ADDRESS, person);
		ech11.dwellingAddress(event, ech11.URI, MAIN_RESIDENCE_ADDRESS, person.dwellingAddress);
		// TODO Spezifikation bei CorrectAddress abklären
		return result();
	}
	
	// code 44
	public String correctRelationship(Person person, boolean includeParents) throws Exception {
        WriterElement event = simplePersonEvent(CORRECT_RELATIONSHIP, person);
        if (includeParents) {
            addParentNames(event, person);
        }
        // Version Relationship <-> relationship
        for (Relation relation: person.relation) ech21.relation(event, _RELATIONSHIP, relation);
        return result();
	}
	
	// code 45
	public String correctOccupation(PersonIdentification personIdentification, List<Occupation> values) throws Exception {
        WriterElement event = simplePersonEvent(CORRECT_OCCUPATION, personIdentification);
        for (Occupation occupation : values) ech21.occupation(event, occupation);
        return result();
	}
	
	// code 46
	public String changeCitizen(PersonIdentification personIdentification, List<PlaceOfOrigin> values) throws Exception {
        WriterElement event = simplePersonEvent(CHANGE_CITIZEN, personIdentification);
        for (PlaceOfOrigin placeOfOrigin : values) ech21.placeOfOriginAddon(event, ORIGIN, placeOfOrigin);
        return result();
	}
	
	// code 47
	public String changeGardian(PersonIdentification personIdentification, Relation relation) throws Exception {
        WriterElement event = simplePersonEvent(CHANGE_GARDIAN, personIdentification);
        relationship(event, RELATIONSHIP, relation);
        return result();
	}
	
	// code 48 (Version 2.0)

	// Ersetzt mit den beiden folgenden Methoden, die zum BaseDelivery
	// kompatibel sind. Sobald ein Export mal validiert wurde kann das
	// gelöscht werden
//	public String keyDelivery(List<PersonIdentification> personIdentifications) throws Exception {
//		WriterElement delivery = delivery();
//		WriterElement keyDelivery = delivery.create(URI, KEY_DELIVERY);
//		keyDelivery.text(NUMBER_OF_KEY_MESSAGES, "" + personIdentifications.size());
//		for (PersonIdentification personIdentification : personIdentifications) {
//			WriterElement messages = keyDelivery.create(URI, MESSAGES); // sic
//			ech44.personIdentification(messages, KEY_PERSON_IDENTIFICATION, personIdentification);
//		}
//		return result();
//	}
	
	public WriterElement keyDelivery(WriterElement delivery, Integer messageCount) throws Exception {
		WriterElement keyDelivery = delivery.create(URI, KEY_DELIVERY);
		keyDelivery.text(NUMBER_OF_MESSAGES, messageCount.toString());
		return keyDelivery;
	}
	
	public void eventKeyDelivery(WriterElement keyDelivery, PersonIdentification personIdentification) throws Exception {
		WriterElement messages = keyDelivery.create(URI, MESSAGES);
		ech44.personIdentification(messages, KEY_PERSON_IDENTIFICATION, personIdentification);
	}
	
	// code 50
	public String correctIdentification(PersonIdentification personIdentification, Person person) throws Exception {
		WriterElement event = delivery().create(URI, CORRECT_IDENTIFICATION);
		correctIdentificationPerson(event, personIdentification, person);
		return result();
	}
	
	private void correctIdentificationPerson(WriterElement event, PersonIdentification personIdentification, Person person) throws Exception {
		WriterElement correctIdentificationPerson = event.create(URI, CORRECT_IDENTIFICATION_PERSON);
		ech44.personIdentification(correctIdentificationPerson, PERSON_IDENTIFICATION_BEFORE, personIdentification);
		ech44.personIdentification(correctIdentificationPerson, PERSON_IDENTIFICATION_AFTER, person.personIdentification());
	}
	
	// code 51
	public String correctName(Person person) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_NAME, person);
		addVariousNames(event, person);
		event.values(person, NAME_ON_PASSPORT);
		return result();
	}

	// code 52
	public String correctNationality(Person person) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_NATIONALITY, person);
        ech11.nationality(event, person.nationality);
        // TODO nationalityFrom
		return result();
	}
	
	// code 53
	public String correctContact(Person person) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_CONTACT, person);
		ech11.contact(event, URI, person, context.contactHasValidTill());
		return result();
	}
	
	// code 54
	public String correctReligion(Person person, Religion religion) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_RELIGION, person);
		event.text(RELIGION, religion);
        // TODO from
		return result();
	}
	
	// code 55
	public String correctOrigin(Person person) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_ORIGIN, person);
		for (PlaceOfOrigin placeOfOrigin : person.placeOfOrigin) ech21.placeOfOriginAddon(event, PLACE_OF_ORIGIN, placeOfOrigin);
		return result();
	}

	// code 56
	public String correctResidencePermit(Person person, Foreign foreign) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_RESIDENCE_PERMIT, person);
        event.values(foreign, RESIDENCE_PERMIT, RESIDENCE_PERMIT_TILL);
        return result();
	}

	// code 57
	public String correctMaritalData(Person person) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_MARITAL_DATA, person);
		ech11.maritalData(event, person);
        return result();
	}

	// code 58
	public String correctPlaceOfBirth(Person person) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_PLACE_OF_BIRTH, person);
		ech11.placeOfBirth(event, person);
        return result();
	}
	
	// code 59
	public String correctDateOfDeath(Person person) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_DATE_OF_DEATH, person);
        event.values(person, DATE_OF_DEATH);
        return result();
	}
	
	// code 60
	public String correctLanguageOfCorrespondance(Person person) throws Exception {
		WriterElement event = simplePersonEvent(CORRECT_LANGUAGE_OF_CORRESPONDANCE, person);
        event.values(person, LANGUAGE_OF_CORRESPONDANCE);
        return result();
	}

}
