package ch.openech.frontend.e44;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.persistence.criteria.By;

import ch.openech.frontend.page.PersonSearchPage;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonSearch;

public class PersonFormElement extends ObjectFormElement<Person> {

	public PersonFormElement(PropertyInterface property) {
		super(property);
	}
	
	public PersonFormElement(Person key) {
		this(Keys.getProperty(key));
	}
	
	@Override
	public Form<Person> createForm() {
		// not used
		return null;
	}

	@Override
	protected void show(Person placeOfOrigin) {
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
			return Backend.read(PersonSearch.class, By.search(query), 100);
		}

	}
}
