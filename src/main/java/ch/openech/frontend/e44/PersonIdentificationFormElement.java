package ch.openech.frontend.e44;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.repository.query.By;

import ch.openech.frontend.page.PersonSearchPage;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;

public class PersonIdentificationFormElement extends ObjectFormElement<PersonIdentification> {

	public PersonIdentificationFormElement(PropertyInterface property) {
		super(property);
	}
	
	@Override
	public Form<PersonIdentification> createForm() {
		return new PersonIdentificationPanel();
	}
	
	@Override
	protected void show(PersonIdentification placeOfOrigin) {
		if (isEditable()) {
			add(placeOfOrigin, new RemoveObjectAction());
		} else {
			add(placeOfOrigin);
		}
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new PersonSearchAction(), new ObjectFormElementEditor() };
	}

	public final class PersonSearchAction extends SearchDialogAction<Person> {
		
		public PersonSearchAction() {
			super(PersonSearchPage.FIELDS);
		}

		@Override
		protected void save(Person object) {
			setValue(object.personIdentification());
		}
		
		@Override
		public List<Person> search(String query) {
			return Backend.find(Person.class, By.search(query).limit(100));
		}
	}
	
}
