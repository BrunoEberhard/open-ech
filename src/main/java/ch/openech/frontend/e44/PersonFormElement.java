package ch.openech.frontend.e44;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.repository.query.By;

import ch.openech.frontend.page.PersonSearchPage;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonSearch;

public class PersonFormElement extends ObjectFormElement<Person> {

	public PersonFormElement(PropertyInterface property) {
		super(property);
	}
	
	public PersonFormElement(Person key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}
	
	@Override
	public Form<Person> createForm() {
		// not used
		return null;
	}

	@Override
	protected void show(Person person) {
		if (isEditable()) {
			add(person, new RemoveObjectAction());
		} else {
			add(person);
		}
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new PersonSearchAction() };
	}

	public class PersonSearchAction extends SearchDialogAction<PersonSearch> {
		
		public PersonSearchAction() {
			super(PersonSearchPage.FIELDS);
		}

		@Override
		protected void save(PersonSearch object) {
			Person person = Backend.read(Person.class, object.id);
			setValue(person);
		}

		@Override
		public List<PersonSearch> search(String query) {
			return Backend.find(PersonSearch.class, By.search(query).limit(100));
		}

	}
}
