package ch.openech.frontend.e44;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.SearchDialogAction;
import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.PropertyInterface;
import org.minimalj.transaction.criteria.Criteria;

import ch.openech.frontend.page.SearchPersonPage;
import ch.openech.model.person.Person;
import ch.openech.model.person.PersonIdentification;

public class PersonIdentificationField extends ObjectFlowField<PersonIdentification> {

	public PersonIdentificationField(PropertyInterface property) {
		super(property);
	}
	
	@Override
	public Form<PersonIdentification> createFormPanel() {
		return new PersonIdentificationPanel();
	}

	@Override
	protected void show(PersonIdentification object) {
		addText(object.toHtml());
	}
	
	@Override
	protected void showActions() {
		if (!isEmpty()) {
			addAction(new RemoveObjectAction());
		}
        addAction(new PersonSearchAction());
        addAction(new ObjectFieldEditor());
	}

	public final class PersonSearchAction extends SearchDialogAction<Person> {
		
		public PersonSearchAction() {
			super(getComponent(), SearchPersonPage.FIELD_NAMES);
		}

		@Override
		protected void save(Person object) {
			setObject(object.personIdentification());
		}
		
		@Override
		public List<Person> search(String searchText) {
			return Backend.getInstance().read(Person.class, Criteria.search(searchText), 100);
		}
	}
	
}
