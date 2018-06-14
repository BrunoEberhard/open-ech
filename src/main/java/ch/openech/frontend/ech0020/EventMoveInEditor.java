package ch.openech.frontend.ech0020;

import java.util.UUID;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.editor.Editor.NewObjectEditor;
import org.minimalj.frontend.form.Form;
import org.minimalj.util.CloneHelper;

import ch.ech.ech0011.v8.MaritalStatus;
import ch.ech.ech0011.v8.NationalityStatus;
import ch.ech.ech0011.v8.Person;
import ch.ech.ech0020.v3.BaseDeliveryPerson;
import ch.ech.ech0020.v3.EventMoveIn;
import ch.ech.ech0044.v4.PersonIdentification;
import ch.openech.frontend.ech0011.PersonForm;

public class EventMoveInEditor extends NewObjectEditor<Person> {

	@Override
	protected Form<Person> createForm() {
		return new PersonForm(Form.EDITABLE);
	}

	@Override
	protected Person createObject() {
		Person person = super.createObject();
		person.maritalData.maritalStatus = MaritalStatus._1;
		person.nationalityData.nationalityStatus = NationalityStatus._0;
		return person;
	}
	
	@Override
	protected Person save(Person person) {
		BaseDeliveryPerson moveInPerson = new BaseDeliveryPerson();

		person.personIdentification = new PersonIdentification();
		// person.personIdentification.id = UUID.randomUUID().toString();
		person.personIdentification.localPersonId.namedIdCategory = "openech";
		person.personIdentification.localPersonId.namedId = UUID.randomUUID().toString();
		person.personIdentification.officialName = person.nameData.officialName;
		person.personIdentification.firstName = person.nameData.firstName;
		person.personIdentification.originalName = person.nameData.originalName;
		person.personIdentification.sex = person.birthData.sex;
		CloneHelper.deepCopy(person.birthData.dateOfBirth, person.personIdentification.dateOfBirth);

		CloneHelper.deepCopy(person.nameData, moveInPerson.nameInfo.nameData);
		CloneHelper.deepCopy(person.birthData, moveInPerson.birthInfo.birthData);
		moveInPerson.birthInfo.birthAddonData = person.birthAddonData;

		EventMoveIn event = new EventMoveIn();
		event.moveInPerson = moveInPerson;

		return Backend.save(person);
	}
	
}
