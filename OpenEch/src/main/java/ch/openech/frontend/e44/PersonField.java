package ch.openech.frontend.e44;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.SearchDialogAction;
import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.IForm;
import org.minimalj.model.Keys;
import org.minimalj.model.PropertyInterface;
import org.minimalj.transaction.criteria.Criteria;

import ch.openech.frontend.page.SearchPersonPage;
import ch.openech.model.person.Person;

public class PersonField extends ObjectFlowField<Person> {

	public PersonField(PropertyInterface property) {
		super(property);
	}
	
	public PersonField(Person key) {
		this(Keys.getProperty(key));
	}
	
	@Override
	public IForm<Person> createFormPanel() {
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
	
	public class PersonSearchAction extends SearchDialogAction<Person> {
		
		public PersonSearchAction() {
			super(getComponent(), SearchPersonPage.FIELD_NAMES);
		}

		@Override
		protected void save(Person object) {
			setObject(object);
		}

		@Override
		public List<Person> search(String searchText) {
			return Backend.getInstance().read(Person.class, Criteria.search(searchText), 100);
		}

	}
}
