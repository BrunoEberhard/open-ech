package ch.openech.frontend.ech0011;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.element.FormElementConstraint;
import org.minimalj.frontend.form.element.ReferenceFormElement;
import org.minimalj.repository.query.By;

import ch.ech.ech0011.Person;
import ch.ech.ech0098.Organisation;

public class ContactReferenceFormElement extends ReferenceFormElement<ContactReference> {

	public ContactReferenceFormElement(ContactReference key) {
		super(key, ContactReference.$.getText());
		height(3, FormElementConstraint.MAX);
	}

	@Override
	public List<ContactReference> search(String query) {
		List<ContactReference> result = new ArrayList<ContactReference>(60);

		List<Person> persons = Backend.find(Person.class, By.search(query));
		persons.stream().forEach(p -> {
			ContactReference reference = new ContactReference();
			reference.person = p;
			result.add(reference);
		});

		List<Organisation> organisations = Backend.find(Organisation.class,
				By.search(query, Organisation.$.organisationIdentification.organisationName, Organisation.$.organisationIdentification.organisationLegalName));
		organisations.stream().forEach(o -> {
			ContactReference reference = new ContactReference();
			reference.organisation = o;
			result.add(reference);
		});

		return result;
	}

}
