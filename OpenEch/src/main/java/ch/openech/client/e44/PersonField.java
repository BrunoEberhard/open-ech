package ch.openech.client.e44;

import java.util.List;

import ch.openech.client.ewk.SearchPersonPage;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.SearchDialogAction;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.server.EchServer;

public class PersonField extends MultiLineObjectField<Person> {

	public PersonField(Object key) {
		super(key);

	}
	
	@Override
	public FormVisual<Person> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void display(Person object) {
		setText(object.personIdentification.toHtml());
		if (isEditable()) {
	        addAction(new PersonSearchAction());
	        addAction(new RemoveObjectAction());
		}
	}

	public class PersonSearchAction extends SearchDialogAction<Person> {
		
		public PersonSearchAction() {
			super(SearchPersonPage.FIELD_NAMES);
		}

		@Override
		protected List<Person> search(String text) {		
			List<Person> resultList = EchServer.getInstance().getPersistence().person().find(text);
			return resultList;
		}

		@Override
		protected void save(Person object) {
			setObject(object);
		}

	}
}
