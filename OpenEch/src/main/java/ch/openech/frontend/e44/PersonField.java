package ch.openech.frontend.e44;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.SearchDialogAction;
import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.Keys;
import org.minimalj.model.PropertyInterface;
import org.minimalj.transaction.criteria.Criteria;

import ch.openech.frontend.page.PersonOrganisationPage;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonSearch;

public class PersonField extends ObjectFlowField<Person> {

	public PersonField(PropertyInterface property) {
		super(property);
	}
	
	public PersonField(Person key) {
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
			super(PersonOrganisationPage.FIELD_NAMES);
		}

		@Override
		protected void save(PersonSearch object) {
			Person person = Backend.getInstance().read(Person.class, object.id);
			setObject(person);
		}

		@Override
		public List<PersonSearch> search(String searchText) {
			return Backend.getInstance().read(PersonSearch.class, Criteria.search(searchText), 100);
		}

	}
}
