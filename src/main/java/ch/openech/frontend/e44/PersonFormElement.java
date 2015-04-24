package ch.openech.frontend.e44;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectPanelFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.transaction.criteria.Criteria;

import ch.openech.frontend.page.PersonSearchPage;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonSearch;

public class PersonFormElement extends ObjectPanelFormElement<Person> {

	public PersonFormElement(PropertyInterface property) {
		super(property);
	}
	
	public PersonFormElement(Person key) {
		this(Keys.getProperty(key));
	}
	
	@Override
	public Form<Person> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void show(Person object) {
		addText(object.toHtml());
	}

	@Override
	protected void showActions() {
        addAction(new PersonSearchAction());
        addAction(new RemoveObjectAction());
	}
	
	public class PersonSearchAction extends SearchDialogAction<PersonSearch> {
		
		public PersonSearchAction() {
			super(PersonSearchPage.FIELD_NAMES);
		}

		@Override
		protected void save(PersonSearch object) {
			Person person = Backend.getInstance().read(Person.class, object.id);
			setValue(person);
		}

		@Override
		public List<PersonSearch> search(String searchText) {
			return Backend.getInstance().read(PersonSearch.class, Criteria.search(searchText), 100);
		}

	}
}
