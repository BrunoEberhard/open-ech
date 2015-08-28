package  ch.openech.model.person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.ViewUtil;
import org.minimalj.model.annotation.Enabled;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Searched;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.EmptyValidator;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.BusinessRule;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;

import ch.openech.model.EchFormats;
import ch.openech.model.Event;
import ch.openech.model.code.NationalityStatus;
import ch.openech.model.common.Address;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.DwellingAddress;
import ch.openech.model.common.Place;
import ch.openech.model.common.TechnicalIds;
import ch.openech.model.contact.ContactEntry;
import ch.openech.model.person.types.DataLock;
import ch.openech.model.person.types.PaperLock;
import ch.openech.model.person.types.PartnerShipAbolition;
import ch.openech.model.person.types.Religion;
import ch.openech.model.person.types.TypeOfRelationship;
import ch.openech.model.person.types.Vn;
import ch.openech.model.types.Language;
import ch.openech.model.types.Sex;
import ch.openech.model.types.TypeOfResidence;


public class Person implements Validation {

	public static final Person $ = Keys.of(Person.class);
	public static final Object[] SEARCH_BY_VN = new Object[]{$.vn.value};
	
	public transient PersonEditMode editMode;

	// Der eCH - Event, mit dem die aktuelle Version der Person erstellt oder
	// verändert wurde
	
	public Event event;
	
	//

	public Object id;
	public int version;
	
	@NotEmpty @Size(EchFormats.baseName) @Searched
	public String firstName, officialName;
	
	@NotEmpty 
	public Sex sex;
	
	@NotEmpty @Searched
	public final DatePartiallyKnown dateOfBirth = new DatePartiallyKnown();

	public final TechnicalIds technicalIds = new TechnicalIds();

	public final Vn vn = new Vn();

	@Size(EchFormats.baseName) @Searched
	public String originalName, alliancePartnershipName, aliasName, otherName, callName;

	public Place placeOfBirth; // 1:0-1 -> not final but a dependable
	public LocalDate dateOfDeath;
	
	public final MaritalStatus maritalStatus = new MaritalStatus(); // 1:1 -> final and MaritalStatus inline
	public final Separation separation = new Separation();
	@Enabled("isMaritalStatusCanceled")
	public PartnerShipAbolition cancelationReason;
	public final Nationality nationality = new Nationality();
	public ContactPerson contactPerson = new ContactPerson();
	
	public Language languageOfCorrespondance = Language.de;
	@NotEmpty
	public Religion religion = Religion.unbekannt;
	@Enabled("!isSwiss")
	public final Foreign foreign = new Foreign();
	
	public TypeOfResidence typeOfResidence = TypeOfResidence.hasMainResidence;
	public final Residence residence = new Residence();
	
	public LocalDate arrivalDate, departureDate;
	public Place comesFrom, goesTo;
	public Address comesFromAddress, goesToAddress;
	public DwellingAddress dwellingAddress = new DwellingAddress(); // Fast immer required, aber nicht bei Birth
	
	public final List<Occupation> occupation = new ArrayList<Occupation>();
	public final List<Relation> relation = new ArrayList<Relation>();
	
	public final NameOfParents nameOfParents = new NameOfParents();
	
	@Enabled("isSwiss")
	public final List<PlaceOfOrigin> placeOfOrigin = new ArrayList<PlaceOfOrigin>();
	
	public DataLock dataLock = DataLock.keine_sperre;
	public PaperLock paperLock = PaperLock.keine_sperre;
	
	public PersonExtendedInformation personExtendedInformation;

	public final List<ContactEntry> contacts = new ArrayList<ContactEntry>();
	
	//	
	
	public Relation getFather() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "father", Relation.class);
		return getRelation(TypeOfRelationship.Vater);
	}
	
	public void setFather(Relation relation) {
		setRelation(TypeOfRelationship.Vater, relation);
	}

	public Relation getMother() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "mother", Relation.class);
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
	
	public boolean isMale() {
		return sex == Sex.maennlich;
	}

	public boolean isFemale() {
		return sex == Sex.weiblich;
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
		return null;
	}

	public boolean isSwiss() {
		if (nationality.nationalityStatus != NationalityStatus.with) return false;
		return nationality.isSwiss();
	}
	
	public boolean isMaritalStatusCanceled() {	
		return maritalStatus.isPartnerschaftAufgeloest() || maritalStatus.isGeschieden();
	}
	
	public boolean isMainResidence() {
		return typeOfResidence == TypeOfResidence.hasMainResidence;
	}
	
	// used in ComboBox
	
	@Override
	public String toString() {
		return firstName + " " + officialName + ", " + dateOfBirth.value;
	}
	
	public String toHtmlMultiLine() {
		StringBuilder s = new StringBuilder();
		toHtmlMultiLine(s);
		return s.toString();
	}
	
	private void toHtmlMultiLine(StringBuilder s) {
		append(s, "Person.id.officialName", officialName);
		append(s, "Person.id.firstName", firstName);
		if (dateOfBirth != null) {
			append(s, "Person.id.dateOfBirth", dateOfBirth.value);
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
			EmptyValidator.validate(resultList, this, $.arrivalDate);
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
				resultList.add(new ValidationMessage($.foreign, "Ausländerkategorie erforderlich"));
			}
		}
	}
	
	private void validatePlaceOfOrigin(List<ValidationMessage> resultList) {
		if (nationality.isSwiss()) {
			if (placeOfOrigin.isEmpty()) {
				resultList.add(new ValidationMessage($.placeOfOrigin, "Heimatort erforderlich"));
			}
		}
	}

	@BusinessRule("Die Geburt einer Person muss nach derjenigen seiner Eltern sein")
	public void validateBirthAfterParents(List<ValidationMessage> resultList) {
		Relation relation = getMother();
		if (relation != null && !isBirthAfterRelation(relation)) {
			resultList.add(new ValidationMessage($.dateOfBirth, "Geburtsdatum muss nach demjenigen der Mutter sein"));
		}
		
		relation = getFather();
		if (relation != null && !isBirthAfterRelation(relation)) {
			resultList.add(new ValidationMessage($.dateOfBirth, "Geburtsdatum muss nach demjenigen des Vaters sein"));
		}
	};

	
	private boolean isBirthAfterRelation(Relation relation) {
		LocalDate localDateOfBirth = dateOfBirth.toLocalDate();
		if (localDateOfBirth == null) return true;
		if (relation == null) return true;
		if (relation.partner == null) return true;
		LocalDate localDateOfBirthPartner = relation.partner.dateOfBirth();
		if (localDateOfBirthPartner == null) return true;
		// strange: isAfter can compare with PartialDate, but is not on PartialDate
		return localDateOfBirth.isAfter(localDateOfBirthPartner); 
	}

	private void validateRelations(List<ValidationMessage> resultList) {
		validateMotherIsFemale(resultList);
		validateFatherIsMale(resultList);
	}

	@BusinessRule("Mutter muss weiblich sein")
	private void validateMotherIsFemale(List<ValidationMessage> resultList) {
		Relation relation = getMother();
		if (relation == null || relation.partner == null || relation.partner.sex() == null) return;
		if (relation.partner.sex() != Sex.weiblich) {
			resultList.add(new ValidationMessage(Person.$.getMother(), "Mutter muss weiblich sein"));
		}
	}
	
	@BusinessRule("Vater muss männlich sein")
	private void validateFatherIsMale(List<ValidationMessage> resultList) {
		Relation relation = getFather();
		if (relation == null || relation.partner == null || relation.partner.sex() == null) return;
		if (relation.partner.sex() != Sex.maennlich) {
			resultList.add(new ValidationMessage(Person.$.getFather(), "Vater muss männlich sein"));
		}
	}
	
	@BusinessRule("Todesdatum darf nicht vor Geburtsdatum sein")
	private void validateDeathNotBeforeBirth(List<ValidationMessage> resultList) {
		LocalDate localDateOfBirth = dateOfBirth.toLocalDate();
		if (localDateOfBirth == null || dateOfDeath == null) return;
		if (dateOfDeath.isBefore(localDateOfBirth)) {
			resultList.add(new ValidationMessage($.dateOfDeath, "Todesdatum darf nicht vor Geburtsdatum sein"));
		}
	}

	@BusinessRule("Ereignis darf nicht vor Geburt der Person stattfinden")
	public static void validateEventNotBeforeBirth(List<ValidationMessage> validationMessages, Person person, LocalDate date, Object key) {
		if (person != null) {
			LocalDate localDateOfBirth = person.dateOfBirth.toLocalDate();
			if (localDateOfBirth != null && date != null) {
				if (date.isBefore(localDateOfBirth)) {
					validationMessages.add(new ValidationMessage(key, "Datum darf nicht vor Geburt der Person sein"));
				}
			}
		}
	}

	@BusinessRule("Die Anzahl von Haupt- und Nebenorten muss dem gewählten Meldeverhältnis entsprechen")
	private void validateResidence(List<ValidationMessage> resultList) {
		String validationMessage = residence.validate(typeOfResidence);
		if (validationMessage != null) {
			resultList.add(new ValidationMessage($.residence, validationMessage));
		}
	}

	public String toHtml() {
		StringBuilder s = new StringBuilder();
		toHtml(s);
		return s.toString();
	}
	
	public void toHtml(StringBuilder s) {
		StringUtils.appendLine(s, firstName, officialName);
		StringUtils.appendLine(s, dateOfBirth.value);
	}
	
	public PersonIdentification personIdentification() {
		PersonIdentification personIdentification = new PersonIdentification();
		ViewUtil.view(this, personIdentification);
		return personIdentification;
	}
	
	public PartnerIdentification partnerIdentification() {
		PartnerIdentification partnerIdentification = new PartnerIdentification();
		partnerIdentification.person = this;
		return partnerIdentification;
	}

}
