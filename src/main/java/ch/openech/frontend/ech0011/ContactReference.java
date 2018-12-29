package ch.openech.frontend.ech0011;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;

import ch.ech.ech0011.Person;
import ch.ech.ech0098.Organisation;

public class ContactReference implements Rendering {

	public static final ContactReference $ = Keys.of(ContactReference.class);

	public Person person;
	public Organisation organisation;

	public String getText() {
		if (Keys.isKeyObject(this))
			return Keys.methodOf(this, "text");

		StringBuilder s = new StringBuilder();
		render(s);
		return s.toString();
	}

	public void render(StringBuilder s) {
		if (person != null) {
			person.render(s);
		} else if (organisation != null) {
			organisation.render(s);
		} else {
			s.append("-\n");
		}
	}

	@Override
	public CharSequence render() {
		return getText();
	}

}
