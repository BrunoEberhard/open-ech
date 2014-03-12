package ch.openech.client.ewk.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.PersonPanel;
import ch.openech.client.page.PersonViewPage;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.common.Place;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonEditMode;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.dm.person.Relation;
import ch.openech.dm.person.types.Religion;
import ch.openech.dm.person.types.TypeOfRelationship;
import ch.openech.dm.types.YesNo;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.mj.page.PageLink;
import ch.openech.mj.server.DbService;
import ch.openech.mj.server.Services;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

// Ein etwas spezieller Event, da er nicht die momentan gewählte Person betrifft,
// sondern als Resultat eine neue Person erstellt.
public class BirthChildEvent extends PersonEventEditor<Person>  {
	private final OpenEchPreferences preferences;
	
	public BirthChildEvent(EchSchema ech, Person person, OpenEchPreferences preferences) {
		super(ech, person);
		this.preferences = preferences;
	}

	@Override
	public IForm<Person> createForm() {
		return new PersonPanel(PersonEditMode.BIRTH, echSchema);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		// not used
	}

	@Override
	public Person load() {
		return calculatePresets(getPerson(), preferences);
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.birth(changedPerson));
	}
	
	@Override
	public Object save(Person object) throws Exception {
		if (super.save(object) != null) {
			return PageLink.link(PersonViewPage.class, echSchema.getVersion(), object.getId());
		} else {
			return SAVE_FAILED;
		}
	}

	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		person.validate(resultList);
		validatePlaceOfBirth(person, resultList);
	}

	@BusinessRule("Geburtsort muss bei Erfassung einer Geburt angegeben werden")
	public void validatePlaceOfBirth(Person person, List<ValidationMessage> resultList) {
		if (echSchema.birthPlaceMustNotBeUnknown() && (person.placeOfBirth == null || person.placeOfBirth.isUnknown())) {
			resultList.add(new ValidationMessage(Person.PERSON.placeOfBirth, "Geburtsort erforderlich"));
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
		if (partnerRelation != null && partnerRelation.partner != null && partnerRelation.partner.vn != null) { 
			List<Person> partnerOfVisiblePersons = Services.get(DbService.class).search(Person.BY_VN, partnerRelation.partner.vn.value);
			if (partnerOfVisiblePersons.size() == 1) {
				Person partnerOfVisiblePerson = partnerOfVisiblePersons.get(0);
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
		relation.partner = parent.personIdentification;
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
			person.personIdentification.officialName = father.personIdentification.officialName;
		} else if (mother != null) {
			person.personIdentification.officialName = mother.personIdentification.officialName;
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
		if (father != null && "2".equals(father.nationality.nationalityStatus)) {
			father.nationality.copyTo(person.nationality);
		} else if (mother != null && "2".equals(mother.nationality.nationalityStatus)) {
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
			newPlaceOfOrigin.cantonAbbreviation.canton = placeOfOrigin.cantonAbbreviation.canton;
			result.add(newPlaceOfOrigin);
		}
		return result;
	}


}
