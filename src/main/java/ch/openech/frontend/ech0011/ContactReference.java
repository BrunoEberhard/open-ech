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

		if (person != null) {
			return person.nameData.officialName;
		} else if (organisation != null) {
			return organisation.organisationIdentification.organisationName;
		} else {
			return "-";
		}
	}

	@Override
	public CharSequence render() {
		return getText();
	}

}
