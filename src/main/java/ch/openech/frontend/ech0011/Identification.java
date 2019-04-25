package ch.openech.frontend.ech0011;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;

import ch.ech.ech0044.PersonIdentification;
import ch.ech.ech0097.OrganisationIdentification;

public class Identification implements Rendering {

	public static final Identification $ = Keys.of(Identification.class);

	public PersonIdentification person;
	public OrganisationIdentification organisation;

	public void set(Object value) {
		person = null;
		organisation = null;
		if (value instanceof PersonIdentification) {
			person = (PersonIdentification) value;
		} else if (value instanceof OrganisationIdentification) {
			organisation = (OrganisationIdentification) value;
		} else if (value != null) {
			throw new IllegalArgumentException(value.toString());
		}
	}

	@Override
	public CharSequence render() {
		if (person != null) {
			return person.render();
		} else if (organisation != null) {
			return organisation.organisationName;
		} else {
			return null;
		}
	}
}
