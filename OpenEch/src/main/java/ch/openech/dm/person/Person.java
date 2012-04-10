package ch.openech.dm.person;

import static ch.openech.dm.EchFormats.baseName;

import java.util.ArrayList;
import java.util.List;

import ch.openech.dm.Event;
import ch.openech.dm.code.MrMrs;
import ch.openech.dm.common.Address;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.Place;
import ch.openech.dm.contact.Contact;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.Reference;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.util.BusinessRule;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

/*
 * Grundsätzlich alles Immutable, Ausnahmen:
 * - die bei der Persistence gemeldeten Entities
 * - die Listen (können nicht Immutable sein)
 * 
 * Ausserdem: Keine doppelt Verschachtelten Tabellen,
 * damit kann man sich SubSubTable ersparen.
 * 
 */
public class Person {

	public static final Person PERSON = Constants.of(Person.class);
	public static final String MR_MRS = "mrMrs";
	public static final String STREET = "street";
	public static final String STREET_NUMBER = "streetNumber";
	public static final String TOWN = "town";

	// Der eCH - Event, mit dem die aktuelle Version der Person erstellt oder
	// verändert wurde
	
	public Event event;
	
	//

	@Reference
	public final PersonIdentification personIdentification = new PersonIdentification();
	
	@FormatName(baseName) 
	public String originalName, alliancePartnershipName, aliasName, otherName, callName;

	public Place placeOfBirth;
	@Date 
	public String dateOfDeath;
	
	public final MaritalStatus maritalStatus = new MaritalStatus();
	public final Separation separation = new Separation();
	@FormatName("partnerShipAbolition")
	public String cancelationReason;
	public final Nationality nationality = new Nationality();
	public final ContactPerson contactPerson = new ContactPerson();
	
	@FormatName("language")
	public String languageOfCorrespondance = "de";
	@Required
	public String religion = "000";
	
	public final Foreign foreign = new Foreign();
	
	@Required
	public String typeOfResidence = "1"; // residence (hasMainResidence / hasSecondaryResidence / hasOtherResidence)
	public final Residence residence = new Residence();
	
	@Date 
	public String arrivalDate, departureDate;
	public Place comesFrom, goesTo;
	public Address comesFromAddress, goesToAddress;
	@Required
	public DwellingAddress dwellingAddress = new DwellingAddress();
	
	public final List<Occupation> occupation = new ArrayList<Occupation>();
	public final List<Relation> relation = new ArrayList<Relation>();
	public final List<PlaceOfOrigin> placeOfOrigin = new ArrayList<PlaceOfOrigin>(); // Nur Swiss
	
	public String dataLock = "0";
	public String paperLock = "0";
	
	public PersonExtendedInformation personExtendedInformation;

	public Contact contact;
	
	//

	public MrMrs getMrMrs() {
		if (isMale()) return MrMrs.Herr;
		else if (isFemale()) return MrMrs.Frau;
		else return null;
	}
	
	public String getDateOfBirth() {
		return personIdentification.dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		personIdentification.dateOfBirth = dateOfBirth;
	}

	// Von PersonPanel gebraucht
	
	public Relation getFather() {
		return getRelation("4");
	}
	
	public void setFather(Relation relation) {
		setRelation("4", relation);
	}

	public Relation getMother() {
		return getRelation("3");
	}
	
	public void setMother(Relation relation) {
		setRelation("3", relation);
	}
	
	private void setRelation(String typeOfRelationship, Relation relation) {
		relation.typeOfRelationship = typeOfRelationship;
		for (Relation r : Person.this.relation) {
			if (StringUtils.equals(r.typeOfRelationship, relation.typeOfRelationship)) {
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
		return StringUtils.isBlank(dateOfDeath);
	}
	
	public boolean isMarried() {
		return maritalStatus.isVerheiratet();
	}
	
	public boolean hasPartner() {
		return maritalStatus.isPartnerschaft();
	}

	public boolean isSeparated() {
		return !StringUtils.isBlank(separation.separation);
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
	
	public Relation getRelation(String type) {
		for (Relation r : relation) {
			if (StringUtils.equals(type, r.typeOfRelationship)) return r;
		}
		Relation newRelation = new Relation();
		newRelation.typeOfRelationship = type;
		if (newRelation.isParent()) newRelation.care = "1";
		relation.add(newRelation);
		return newRelation;
	}

	public boolean isSwiss() {
		if (!"2".equals(nationality.nationalityStatus)) return false;
		return nationality.nationalityCountry.isSwiss();
	}
	
	public boolean isMainResidence() {
		return "1".equals(typeOfResidence);
	}
	
	public String getFatherFirstNameAtBirth() {
		return getFather().firstNameAtBirth;
	}

	public String getFatherOfficialNameAtBirth() {
		return getFather().officialNameAtBirth;
	}

	public String getMotherFirstNameAtBirth() {
		return getMother().firstNameAtBirth;
	}

	public String getMotherOfficialNameAtBirth() {
		return getMother().officialNameAtBirth;
	}
	
	public void setFatherFirstNameAtBirth(String text) {
		getFather().firstNameAtBirth = text;
	}

	public void setFatherOfficialName(String text) {
		getFather().officialNameAtBirth = text;
	}

	public void setMotherFirstNameAtBirth(String text) {
		getMother().firstNameAtBirth = text;
	}

	public void setMotherOfficialNameAtBirth(String text) {
		getMother().officialNameAtBirth = text;
	}
	
	public String getStreet() {
		if (dwellingAddress != null && dwellingAddress.mailAddress != null) {
			return dwellingAddress.mailAddress.street;
		} else {
			return null;
		}
	}

	public String getStreetNumber() {
		if (dwellingAddress != null && dwellingAddress.mailAddress != null) {
			return dwellingAddress.mailAddress.houseNumber.houseNumber;
		} else {
			return null;
		}
	}

	public String getTown() {
		if (dwellingAddress != null && dwellingAddress.mailAddress != null) {
			return dwellingAddress.mailAddress.town;
		} else {
			return null;
		}
	}
	
	// used in ComboBox
	
	@Override
	public String toString() {
		return personIdentification.firstName + " " + personIdentification.officialName + ", " + DateUtils.formatCH(getDateOfBirth());
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
		if (DateUtils.isValueValidUS(personIdentification.dateOfBirth)) {
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
	}
	
	@BusinessRule("Die Geburt einer Person muss nach derjenigen seiner Eltern sein")
	private void validateBirthAfterParents(List<ValidationMessage> resultList) {
		Relation relation = getMother();
		if (!isBirthAfterRelation(relation)) {
			resultList.add(new ValidationMessage(PERSON.personIdentification + "." + PersonIdentification.PERSON_IDENTIFICATION.dateOfBirth, "Geburtsdatum muss nach demjenigen der Mutter sein"));
		}
		
		relation = getFather();
		if (!isBirthAfterRelation(relation)) {
			resultList.add(new ValidationMessage(PERSON.personIdentification + "." + PersonIdentification.PERSON_IDENTIFICATION.dateOfBirth, "Geburtsdatum muss nach demjenigen des Vaters sein"));
		}
	}

	private boolean isBirthAfterRelation(Relation relation) {
		if (StringUtils.isBlank(getDateOfBirth())) return true;
		if (relation == null) return true;
		PersonIdentification partner = relation.partner;
		if (partner == null) return true;
		if (StringUtils.isBlank(partner.dateOfBirth)) return true;
		return getDateOfBirth().compareTo(partner.dateOfBirth) > 0; 
	}

	private void validateRelations(List<ValidationMessage> resultList) {
		validateMotherIsFemale(resultList);
		validateFatherIsMale(resultList);
	}

	@BusinessRule("Mutter muss weiblich sein")
	private void validateMotherIsFemale(List<ValidationMessage> resultList) {
		Relation relation = getMother();
		if (relation == null || relation.partner == null || StringUtils.isBlank(relation.partner.sex)) return;
		if (!relation.partner.isFemale()) {
			resultList.add(new ValidationMessage("mother", "Mutter muss weiblich sein"));
		}
	}
	
	@BusinessRule("Vater muss männlich sein")
	private void validateFatherIsMale(List<ValidationMessage> resultList) {
		Relation relation = getFather();
		if (relation == null || relation.partner == null || StringUtils.isBlank(relation.partner.sex)) return;
		if (!relation.partner.isMale()) {
			resultList.add(new ValidationMessage("father", "Vater muss männlich sein"));
		}
	}
	
	@BusinessRule("Todesdatum darf nicht vor Geburtsdatum sein")
	private void validateDeathNotBeforeBirth(List<ValidationMessage> resultList) {
		if (StringUtils.isBlank(getDateOfBirth()) || StringUtils.isBlank(dateOfDeath)) return;
		if (getDateOfBirth().compareTo(dateOfDeath) > 0) {
			resultList.add(new ValidationMessage(PERSON.dateOfDeath, "Todesdatum darf nicht vor Geburtsdatum sein"));
		}
	}

	@BusinessRule("Ereignis darf nicht vor Geburt der Person stattfinden")
	public static void validateEventNotBeforeBirth(List<ValidationMessage> validationMessages, PersonIdentification personIdentification, String date, String key) {
		if (personIdentification != null && !StringUtils.isBlank(personIdentification.dateOfBirth) && !StringUtils.isBlank(date)) {
			if (date.compareTo(personIdentification.dateOfBirth) < 0) {
				validationMessages.add(new ValidationMessage(key, "Datum darf nicht vor Geburt der Person sein"));
			}
		}
	}

	
}
