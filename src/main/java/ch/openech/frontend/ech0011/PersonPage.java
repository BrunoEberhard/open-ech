package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.ObjectPage;

import ch.ech.ech0011.Person;

public class PersonPage extends ObjectPage<Person> {

	public PersonPage(Person object) {
		super(object);
	}

	@Override
	protected Form<Person> createForm() {
		return new PersonForm(false);
	}

}
