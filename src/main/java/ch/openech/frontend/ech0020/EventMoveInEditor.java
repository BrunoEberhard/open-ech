package ch.openech.frontend.ech0020;

import java.util.UUID;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.editor.Editor.NewObjectEditor;
import org.minimalj.frontend.form.Form;
import org.minimalj.util.CloneHelper;

import ch.ech.ech0011.MaritalStatus;
import ch.ech.ech0011.NationalityStatus;
import ch.ech.ech0011.Person;
import ch.ech.ech0044.PersonIdentification;
import ch.ech.ech0044.Sex;
import ch.openech.frontend.ech0011.PersonForm;

public class EventMoveInEditor extends NewObjectEditor<Person> {

	@Override
	protected Form<Person> createForm() {
		return new PersonForm(Form.EDITABLE);
	}

	@Override
	protected Person createObject() {
		Person person = new Person();
		person.personIdentification = new PersonIdentification();
		person.personIdentification.localPersonId.namedIdCategory = "openech";
		person.personIdentification.localPersonId.namedId = UUID.randomUUID().toString();
		person.personIdentification.officialName = "filledOnSave";
		person.personIdentification.firstName = "filledOnSave";
		person.personIdentification.sex = Sex._1;
		person.maritalData.maritalStatus = MaritalStatus._1;
		person.nationalityData.nationalityStatus = NationalityStatus._0;

		person.religionData.religion = "666";

		person.maritalData.maritalStatus = MaritalStatus._1;

		person.nationalityData.nationalityStatus = NationalityStatus._1;

//		person.residencePermit.residencePermit = ResidencePermit._01;

//		person.lockData.dataLock = DataLock._0;
//		person.lockData.paperLock = YesNo._1;

		return person;
	}
	
	@Override
	protected Person save(Person person) {

		person.personIdentification.officialName = person.nameData.officialName;
		person.personIdentification.firstName = person.nameData.firstName;
		person.personIdentification.originalName = person.nameData.originalName;
		person.personIdentification.sex = person.birthData.sex;
		CloneHelper.deepCopy(person.birthData.dateOfBirth, person.personIdentification.dateOfBirth);

//		CloneHelper.deepCopy(person.nameData, moveInPerson.nameInfo.nameData);
//		CloneHelper.deepCopy(person.birthData, moveInPerson.birthInfo.birthData);
//		moveInPerson.birthInfo.birthAddonData = person.birthAddonData;

//		EventMoveIn event = new EventMoveIn();
//		event.moveInPerson = moveInPerson;

		// TODO do this in one specialized Transaction
		person = Backend.save(person);
		person.personIdentification.localPersonId.namedId = person.id.toString();
		person.personIdentification.localPersonId.namedIdCategory = "OpenEchPerson";
		Backend.update(person.personIdentification);

		return person;
	}
	
}
