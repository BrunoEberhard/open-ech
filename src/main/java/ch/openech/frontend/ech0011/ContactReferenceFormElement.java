package ch.openech.frontend.ech0011;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.element.ReferenceFormElement;
import org.minimalj.repository.query.By;

import ch.ech.ech0011.Person;

public class ContactReferenceFormElement extends ReferenceFormElement<ContactReference> {

	public ContactReferenceFormElement(ContactReference key, boolean editable) {
		super(key, ContactReference.$.getText());
	}

	@Override
	public List<ContactReference> search(String searchText) {
		List<Person> persons = Backend.find(Person.class, By.search(searchText));
		List<ContactReference> result = new ArrayList<>();
		for (Person person : persons) {
			ContactReference r = new ContactReference();
			r.person = person;
			result.add(r);
		}
		return result;
	}

//	@Override
//	protected String render(Object value) {
//		if (value instanceof Person) {
//			Person person = (Person) value;
//			return person.nameData.firstName + " " + person.nameData.officialName;
//		} else {
//			return "" + value;
//		}
//	}

}
