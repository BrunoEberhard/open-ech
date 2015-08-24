package ch.openech.frontend.ewk.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.transaction.criteria.Criteria;
import org.minimalj.util.BusinessRule;
import org.minimalj.util.CloneHelper;

import ch.openech.frontend.ewk.PersonPanel;
import ch.openech.frontend.page.PersonPage;
import ch.openech.frontend.preferences.OpenEchPreferences;
import ch.openech.model.code.NationalityStatus;
import ch.openech.model.common.Place;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonEditMode;
import ch.openech.model.person.PersonIdentification;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.model.person.Relation;
import ch.openech.model.person.types.Religion;
import ch.openech.model.person.types.TypeOfRelationship;
import ch.openech.model.types.YesNo;
import ch.openech.xml.write.WriterEch0020;

// Ein etwas spezieller Event, da er nicht die momentan gewählte Person betrifft,
// sondern als Resultat eine neue Person erstellt.
public class BirthChildEvent extends PersonEventEditor<Person>  {
	private final OpenEchPreferences preferences;
	
	public BirthChildEvent(PersonPage personPage, OpenEchPreferences preferences) {
		super(personPage);
		this.preferences = preferences;
	}

	@Override
	public Form<Person> createForm() {
		return new PersonPanel(PersonEditMode.BIRTH, echSchema);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		// not used
	}

	@Override
	public Person createObject() {
		return calculatePresets(getPerson(), preferences);
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.birth(changedPerson));
	}
	
	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		person.validate(resultList);
		validatePlaceOfBirth(person, resultList);
	}

	@BusinessRule("Geburtsort muss bei Erfassung einer Geburt angegeben werden")
	public void validatePlaceOfBirth(Person person, List<ValidationMessage> resultList) {
		if (echSchema.birthPlaceMustNotBeUnknown() && (person.placeOfBirth == null || person.placeOfBirth.isUnknown())) {
			resultList.add(new ValidationMessage(Person.$.placeOfBirth, "Geburtsort erforderlich"));
		}
	}
	
	//
	
	private static Person calculatePresets(Person parentPerson, OpenEchPreferences preferences) {
		Person person = new Person();

		Person mother = null;
		Person father = null;

		if (parentPerson.isMale())
			father = parentPerson;
		else
			mother = parentPerson;
		
		Relation partnerRelation = parentPerson.getPartner();
		if (partnerRelation != null && partnerRelation.partner.vn() != null) { 
			List<PersonIdentification> partnerOfVisiblePersons = Backend.persistence().read(PersonIdentification.class, Criteria.search(partnerRelation.partner.vn().value, Person.SEARCH_BY_VN), 2);
			if (partnerOfVisiblePersons.size() == 1) {
				Person partnerOfVisiblePerson = Backend.persistence().read(Person.class, partnerOfVisiblePersons.get(0).id);
				if (partnerOfVisiblePerson.isMale())
					father = partnerOfVisiblePerson;
				else
					mother = partnerOfVisiblePerson;
			}
		}

		if (mother != null) {
			addRelation(person, mother);
		}

		if (father != null) {
			addRelation(person, father);
		}

		presetOfficialName(person, father, mother);
		presetReligion(person, father, mother);
		presetNationality(person, father, mother);
		presetPlaceOfOrigin(person, father, mother);
		// presetContact(person, father, mother);

		if (preferences.preferencesDefaultsData.residence != null) {
			person.placeOfBirth = new Place();
			person.placeOfBirth.municipalityIdentification = CloneHelper.clone(preferences.preferencesDefaultsData.residence);
		}
		return person;
	}

	private static void addRelation(Person person, Person parent) {
		Relation relation = new Relation();
		relation.partner.setValue(parent);
		if (parent.isMale()) {
			relation.typeOfRelationship = TypeOfRelationship.Vater;
		} else if (parent.isFemale()) {
			relation.typeOfRelationship = TypeOfRelationship.Mutter;
		}
		relation.care = YesNo.Yes;
		person.relation.add(relation);
	}

	@BusinessRule("Bei Geburt wird offizieller Name von Vater übernommen, wenn nicht vorhanden von Mutter")
	private static void presetOfficialName(Person person, Person father, Person mother) {
		if (father != null) {
			person.officialName = father.officialName;
		} else if (mother != null) {
			person.officialName = mother.officialName;
		}
	}

	// @BusinessRule("Bei Geburt wird Kontakt von Mutter übernommen, wenn Mutter unbekannt vom Vater")
	// private void presetContact(Person person, Person father, Person mother) {
	// if (mother != null) {
	// person.dwellingAddress = mother.dwellingAddress;
	// person.contactPerson = mother.contactPerson;
	// person.contactPerson.address = mother.contactAddress;
	// } else if (father != null) {
	// person.dwellingAddress = father.dwellingAddress;
	// person.contactPerson = father.contactPerson;
	// person.contactPerson.address = father.contactAddress;
	// }
	// }

	@BusinessRule("Bei Geburt wird Religion von Vater übernommen, wenn nicht vorhanden von Mutter")
	private static void presetReligion(Person person, Person father, Person mother) {
		if (father != null && father.religion != null && father.religion != Religion.unbekannt) {
			person.religion = father.religion;
		} else if (mother != null && mother.religion != null) {
			person.religion = mother.religion;
		}
	}

	@BusinessRule("Bei Geburt wird Staatsangehörigkeit von Vater übernommen, wenn nicht vorhanden von Mutter")
	private static void presetNationality(Person person, Person father, Person mother) {
		if (father != null && father.nationality.nationalityStatus == NationalityStatus.with) {
			father.nationality.copyTo(person.nationality);
		} else if (mother != null && mother.nationality.nationalityStatus == NationalityStatus.with) {
			mother.nationality.copyTo(person.nationality);
		}
	}

	@BusinessRule("Bei Geburt wird Heimatort von Vater übernommen, wenn nicht vorhanden oder ausländisch von Mutter")
	private static void presetPlaceOfOrigin(Person person, Person father, Person mother) {
		if (father != null && father.isSwiss()) {
			person.placeOfOrigin.addAll(convertToAbstammung(father.placeOfOrigin));
		} else if (mother != null && mother.isSwiss()) {
			person.placeOfOrigin.addAll(convertToAbstammung(mother.placeOfOrigin));
		}
	}

	private static List<PlaceOfOrigin> convertToAbstammung(List<PlaceOfOrigin> placeOfOrigins) {
		List<PlaceOfOrigin> result = new ArrayList<PlaceOfOrigin>();
		for (PlaceOfOrigin placeOfOrigin : placeOfOrigins) {
			// Abstammung und naturalizationDate = dateOfBirth wird später auf
			// dem Server gesetzt. Das XML lässt eine übertragung das
			// placeOfOriginAddOn
			// auch gar nicht zu
			PlaceOfOrigin newPlaceOfOrigin = new PlaceOfOrigin();
			newPlaceOfOrigin.originName = placeOfOrigin.originName;
			newPlaceOfOrigin.canton = placeOfOrigin.canton;
			result.add(newPlaceOfOrigin);
		}
		return result;
	}


}
