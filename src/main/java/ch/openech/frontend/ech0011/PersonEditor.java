package ch.openech.frontend.ech0011;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.editor.Editor.SimpleEditor;
import org.minimalj.frontend.form.Form;
import org.minimalj.util.CloneHelper;

import ch.ech.ech0011.Person;
import ch.ech.ech0044.PersonIdentification;

public class PersonEditor extends SimpleEditor<Person> {

	@Override
	protected Person createObject() {
		Person person = new Person();
		person.personIdentification = new PersonIdentification();
		return person;
	}

	@Override
	protected Form<Person> createForm() {
		return new PersonForm(true);
	}

	@Override
	protected Person save(Person person) {
		person.personIdentification.officialName = person.nameData.officialName;
		person.personIdentification.firstName = person.nameData.firstName;
		person.personIdentification.originalName = person.nameData.originalName;
		person.personIdentification.sex = person.birthData.sex;
		CloneHelper.deepCopy(person.birthData.dateOfBirth, person.personIdentification.dateOfBirth);
		return Backend.save(person);
	}

}
