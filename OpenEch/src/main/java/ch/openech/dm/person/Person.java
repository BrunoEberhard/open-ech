package ch.openech.dm.person;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import ch.openech.dm.EchFormats;
import ch.openech.dm.Event;
import ch.openech.dm.code.NationalityStatus;
import ch.openech.dm.common.Address;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.Place;
import ch.openech.dm.contact.Contact;
import ch.openech.dm.person.types.PartnerShipAbolition;
import ch.openech.dm.person.types.Religion;
import ch.openech.dm.person.types.TypeOfRelationship;
import ch.openech.dm.types.Language;
import ch.openech.dm.types.TypeOfResidence;
import ch.openech.dm.types.YesNo;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.EmptyValidator;
import ch.openech.mj.edit.validation.Validation;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.Reference;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.model.annotation.Changes;
import ch.openech.mj.model.annotation.Code;
import ch.openech.mj.model.annotation.Enabled;
import ch.openech.mj.model.annotation.OnChange;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.util.BusinessRule;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;


public class Person implements Validation {

	public static final Person PERSON = Constants.of(Person.class);

	public transient PersonEditMode editMode;

	// Der eCH - Event, mit dem die aktuelle Version der Person erstellt oder
	// verändert wurde
	
	public Event event;
	
	//

	@Reference
	public final PersonIdentification personIdentification = new PersonIdentification();
	
	@Size(EchFormats.baseName)
	public String originalName, alliancePartnershipName, aliasName, otherName, callName;

	public Place placeOfBirth;
	public LocalDate dateOfDeath;
	
	public final MaritalStatus maritalStatus = new MaritalStatus();
	public final Separation separation = new Separation();
	@Enabled("isMaritalStatusCanceled")
	public PartnerShipAbolition cancelationReason;
	public final Nationality nationality = new Nationality();
	public final ContactPerson contactPerson = new ContactPerson();
	
	public Language languageOfCorrespondance = Language.de;
	@Required
	public Religion religion = Religion.unbekannt;
	@Enabled("!isSwiss")
	public final Foreign foreign = new Foreign();
	
	@OnChange("updateResidence")
	public TypeOfResidence typeOfResidence = TypeOfResidence.hasMainResidence;
	public final Residence residence = new Residence();
	
	@Changes("residence")
	public void updateResidence() {
		if (typeOfResidence == TypeOfResidence.hasOtherResidence) {
			residence.reportingMunicipality = null;
		}
	}
	
	public LocalDate arrivalDate, departureDate;
	public Place comesFrom, goesTo;
	public Address comesFromAddress, goesToAddress;
	@Required
	public DwellingAddress dwellingAddress = new DwellingAddress();
	
	public final List<Occupation> occupation = new ArrayList<Occupation>();
	public final List<Relation> relation = new ArrayList<Relation>();
	
	public final NameOfParents nameOfParents = new NameOfParents();
	
	@Enabled("isSwiss")
	public final List<PlaceOfOrigin> placeOfOrigin = new ArrayList<PlaceOfOrigin>();
	
	@Code
	public String dataLock = "0";
	@Code
	public String paperLock = "0";
	
	public PersonExtendedInformation personExtendedInformation;

	public Contact contact;
	
	// 
	
	public Relation getFather() {
		if (Constants.isKeyObject(this)) return Constants.methodOf(this, "father", Relation.class);
		return getRelation(TypeOfRelationship.Vater);
	}
	
	public void setFather(Relation relation) {
		setRelation(TypeOfRelationship.Vater, relation);
	}

	public Relation getMother() {
		if (Constants.isKeyObject(this)) return Constants.methodOf(this, "mother", Relation.class);
		return getRelation(TypeOfRelationship.Mutter);
	}
	
	public void setMother(Relation relation) {
		setRelation(TypeOfRelationship.Mutter, relation);
	}
	
	private void setRelation(TypeOfRelationship typeOfRelationship, Relation relation) {
		for (Relation r : Person.this.relation) {
			if (r.typeOfRelationship == relation.typeOfRelationship) {
				Person.this.relation.remove(r);
				break;
			}
		}
		Person.this.relation.add(relation);
	}
	
	//
	
	public String getId() {
		return personIdentification.getId();
	}
	
	public boolean isPersisent() {
		return personIdentification != null;
	}
	
	public boolean isMale() {
		return personIdentification.isMale();
	}

	public boolean isFemale() {
		return personIdentification.isFemale();
	}
	
	public boolean isAlive() {
		return dateOfDeath == null;
	}
	
	public boolean isMarried() {
		return maritalStatus.isVerheiratet();
	}
	
	public boolean hasPartner() {
		return maritalStatus.isPartnerschaft();
	}

	public boolean isSeparated() {
		return separation.separation != null;
	}

	public boolean hasGardianMeasure() {
		return getGardian() != null;
	}
	
	// Ehepartner oder Partner in eingetragener Partnerschaft
	public Relation getPartner() {
		for (Relation r : relation) {
			if (r.isPartnerType()) return r;
		}
		return null;
	}
	
	// Beistand / Beirat oder Vormund
	public Relation getGardian() {
		for (Relation r : relation) {
			if (r.isCareRelation()) return r;
		}
		return null;
	}
	
	public Relation getRelation(TypeOfRelationship type) {
		for (Relation r : relation) {
			if (type == r.typeOfRelationship) return r;
		}
		Relation newRelation = new Relation();
		newRelation.typeOfRelationship = type;
		if (newRelation.isParent()) newRelation.care = YesNo.Yes;
		relation.add(newRelation);
		return newRelation;
	}

	public boolean isSwiss() {
		if (nationality.nationalityStatus != NationalityStatus.with) return false;
		return nationality.nationalityCountry.isSwiss();
	}
	
	public boolean isMaritalStatusCanceled() {	
		return maritalStatus.isPartnerschaftAufgeloest() || maritalStatus.isGeschieden();
	}
	
	public boolean isMainResidence() {
		return typeOfResidence == TypeOfResidence.hasMainResidence;
	}
	
	public String getStreet() {
		if (Constants.isKeyObject(this)) return Constants.methodOf(this, "street", String.class);
		if (dwellingAddress != null && dwellingAddress.mailAddress != null) {
			return dwellingAddress.mailAddress.street;
		} else {
			return null;
		}
	}

	public String getStreetNumber() {
		if (Constants.isKeyObject(this)) return Constants.methodOf(this, "streetNumber", String.class);
		if (dwellingAddress != null && dwellingAddress.mailAddress != null) {
			return dwellingAddress.mailAddress.houseNumber.houseNumber;
		} else {
			return null;
		}
	}

	public String getTown() {
		if (Constants.isKeyObject(this)) return Constants.methodOf(this, "town", String.class);
		if (dwellingAddress != null && dwellingAddress.mailAddress != null) {
			return dwellingAddress.mailAddress.town;
		} else {
			return null;
		}
	}
	
	// used in ComboBox
	
	@Override
	public String toString() {
		return personIdentification.firstName + " " + personIdentification.officialName + ", " + DateUtils.formatCH(personIdentification.dateOfBirth);
	}
	
	public String toHtmlMultiLine() {
		StringBuilder s = new StringBuilder();
		s.append("<HTML>");
		toHtmlMultiLine(s);
		s.append("</HTML>");
		return s.toString();
	}
	
	private void toHtmlMultiLine(StringBuilder s) {
		append(s, "Person.officialName", personIdentification.officialName);
		append(s, "Person.firstName", personIdentification.firstName);
		if (personIdentification.dateOfBirth != null) {
			append(s, "Person.dateOfBirth", DateUtils.formatCH(personIdentification.dateOfBirth));
		}
		s.append("<BR>&nbsp;");
	}

	private void append(StringBuilder s, String resourceName, String text) {
		String labelText = Resources.getString(resourceName);
		s.append(labelText);
		s.append(": ");
		if (!StringUtils.isBlank(text)) {
			s.append(text);
		} else {
			s.append("-");
		}
		s.append("<BR>");
	}

	// validation
	
	public void validate(List<ValidationMessage> resultList) {
		validateBirthAfterParents(resultList);
		validateRelations(resultList);
		validateDeathNotBeforeBirth(resultList);
		
		if (editMode == PersonEditMode.MOVE_IN) {
			EmptyValidator.validate(resultList, this, PERSON.arrivalDate);
		}
		
		if (editMode != PersonEditMode.BIRTH) {
			validateForeign(resultList);
		}
		
		validatePlaceOfOrigin(resultList);

		if (editMode != PersonEditMode.BIRTH) {
			validateResidence(resultList);
		}
	}

	private void validateForeign(List<ValidationMessage> resultList) {
		if (!nationality.isSwiss()) {
			if (foreign.isEmpty()) {
				resultList.add(new ValidationMessage(PERSON.foreign, "Ausländerkategorie erforderlich"));
			}
		}
	}
	
	private void validatePlaceOfOrigin(List<ValidationMessage> resultList) {
		if (nationality.isSwiss()) {
			if (placeOfOrigin.isEmpty()) {
				resultList.add(new ValidationMessage(PERSON.placeOfOrigin, "Heimatort erforderlich"));
			}
		}
	}

	@BusinessRule("Die Geburt einer Person muss nach derjenigen seiner Eltern sein")
	public void validateBirthAfterParents(List<ValidationMessage> resultList) {
		Relation relation = getMother();
		if (!isBirthAfterRelation(relation)) {
			resultList.add(new ValidationMessage(PERSON.personIdentification.dateOfBirth, "Geburtsdatum muss nach demjenigen der Mutter sein"));
		}
		
		relation = getFather();
		if (!isBirthAfterRelation(relation)) {
			resultList.add(new ValidationMessage(PERSON.personIdentification.dateOfBirth, "Geburtsdatum muss nach demjenigen des Vaters sein"));
		}
	};

	
	private boolean isBirthAfterRelation(Relation relation) {
		if (personIdentification.dateOfBirth == null) return true;
		if (relation == null) return true;
		PersonIdentification partner = relation.partner;
		if (partner == null) return true;
		if (partner.dateOfBirth == null) return true;
		return personIdentification.dateOfBirth.isAfter(partner.dateOfBirth); 
	}

	private void validateRelations(List<ValidationMessage> resultList) {
		validateMotherIsFemale(resultList);
		validateFatherIsMale(resultList);
	}

	@BusinessRule("Mutter muss weiblich sein")
	private void validateMotherIsFemale(List<ValidationMessage> resultList) {
		Relation relation = getMother();
		if (relation == null || relation.partner == null || relation.partner.sex == null) return;
		if (!relation.partner.isFemale()) {
			resultList.add(new ValidationMessage(Person.PERSON.getMother(), "Mutter muss weiblich sein"));
		}
	}
	
	@BusinessRule("Vater muss männlich sein")
	private void validateFatherIsMale(List<ValidationMessage> resultList) {
		Relation relation = getFather();
		if (relation == null || relation.partner == null || relation.partner.sex == null) return;
		if (!relation.partner.isMale()) {
			resultList.add(new ValidationMessage(Person.PERSON.getFather(), "Vater muss männlich sein"));
		}
	}
	
	@BusinessRule("Todesdatum darf nicht vor Geburtsdatum sein")
	private void validateDeathNotBeforeBirth(List<ValidationMessage> resultList) {
		if (personIdentification.dateOfBirth == null || dateOfDeath == null) return;
		if (dateOfDeath.isBefore(personIdentification.dateOfBirth)) {
			resultList.add(new ValidationMessage(PERSON.dateOfDeath, "Todesdatum darf nicht vor Geburtsdatum sein"));
		}
	}

	@BusinessRule("Ereignis darf nicht vor Geburt der Person stattfinden")
	public static void validateEventNotBeforeBirth(List<ValidationMessage> validationMessages, PersonIdentification personIdentification, LocalDate date, Object key) {
		if (personIdentification != null && personIdentification.dateOfBirth != null && date != null) {
			if (date.isBefore(personIdentification.dateOfBirth)) {
				validationMessages.add(new ValidationMessage(key, "Datum darf nicht vor Geburt der Person sein"));
			}
		}
	}

	@BusinessRule("Die Anzahl von Haupt- und Nebenorten muss dem gewählten Meldeverhältnis entsprechen")
	private void validateResidence(List<ValidationMessage> resultList) {
		String validationMessage = residence.validate(typeOfResidence);
		if (validationMessage != null) {
			resultList.add(new ValidationMessage(PERSON.residence, validationMessage));
		}
	}
	
}
