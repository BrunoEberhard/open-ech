package ch.openech.client.e44;

import java.util.List;

import ch.openech.client.page.SearchPersonPage;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.SearchDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.server.DbService;
import ch.openech.mj.server.Services;

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
		public List<Person> search(String query) {
			return Services.get(DbService.class).search(Person.class, query, 100);
		}

	}
}
