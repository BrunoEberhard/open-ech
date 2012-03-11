package ch.openech.client.e44;

import java.util.List;

import ch.openech.client.ewk.SearchPersonPage;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.SearchDialogAction;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.MultiLineTextField;
import ch.openech.server.EchServer;

public class PersonField extends ObjectField<Person> {
	protected final MultiLineTextField text;

	public PersonField(Object key) {
		super(key);
		text = ClientToolkit.getToolkit().createMultiLineTextField();
		
		// editable 
        addAction(new PersonSearchAction());
        addAction(new RemoveObjectAction());
	}
	
	@Override
	protected IComponent getComponent0() {
		return text;
	}

	@Override
	public FormVisual<Person> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void display(Person object) {
		if (object != null) {
			text.setText(object.personIdentification.toHtml());
		} else {
			text.setText(null);
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
