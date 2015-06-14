package ch.openech.frontend.e44;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.editor.SearchDialogAction;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.transaction.criteria.Criteria;

import ch.openech.frontend.page.PersonSearchPage;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;

public class PersonIdentificationFormElement extends ObjectFormElement<PersonIdentification> {

	public PersonIdentificationFormElement(PropertyInterface property) {
		super(property);
	}
	
	@Override
	public Form<PersonIdentification> createFormPanel() {
		return new PersonIdentificationPanel();
	}
	
	@Override
	protected void show(PersonIdentification placeOfOrigin) {
		add(placeOfOrigin, new RemoveObjectAction());
	}

	@Override
	protected Action[] getActions() {
		return new Action[] { new PersonSearchAction(), new ObjectFormElementEditor() };
	}

	public final class PersonSearchAction extends SearchDialogAction<Person> {
		
		public PersonSearchAction() {
			super(PersonSearchPage.FIELD_NAMES);
		}

		@Override
		protected void save(Person object) {
			setValue(object.personIdentification());
		}
		
		@Override
		public List<Person> search(String searchText) {
			return Backend.getInstance().read(Person.class, Criteria.search(searchText), 100);
		}
	}
	
}