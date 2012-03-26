package ch.openech.client.e44;

import java.util.List;

import ch.openech.client.ewk.SearchPersonPage;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.SearchDialogAction;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.server.EchServer;

public class PersonIdentificationField extends MultiLineObjectField<PersonIdentification> {

	public PersonIdentificationField(Object key) {
		super(key);
	}
	
	@Override
	public FormVisual<PersonIdentification> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void display(PersonIdentification object) {
		if (object != null) {
			setText(object.toHtml());
			addAction(new RemoveObjectAction());
		} else {
			clearVisual();
		}
		// editable 
        addAction(new PersonSearchAction());
        // EditObjectAction kann nicht verwendet werden, sondern die PersonIdentificationEditAction,
        // weil der Typ vom FormPanel PersonIdentification und nicht Person ist
        addAction(new PersonIdentificationEditor());
	}

//	public final class PersonSearchEditor extends Editor<Person> {
//		
//		@Override
//		protected FormVisual<Person> createForm() {
//			return new PersonSearchForm();
//		}
//
//		@Override
//		protected Person load() {
//			// not used
//			return null;
//		}
//
//		@Override
//		protected void validate(Person object, List<ValidationMessage> resultList) {
//			// not used
//		}
//
//		@Override
//		protected boolean save(Person person) {
//			if (person != null) {
//				PersonIdentificationField.this.setObject(person.personIdentification);
//			}
//			return true;
//		}
//	}
	
	public final class PersonSearchAction extends SearchDialogAction<Person> {
		
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
			setObject(object.personIdentification);
		}
	}
	
	public class PersonIdentificationEditor extends Editor<PersonIdentification> {
		
		@Override
		public FormVisual<PersonIdentification> createForm() {
			return new PersonIdentificationPanel();
		}

		@Override
		public PersonIdentification load() {
			return PersonIdentificationField.this.getObject();
		}

		@Override
		public boolean save(PersonIdentification personIdentification) {
			PersonIdentificationField.this.setObject(personIdentification);
			return true;
		}

		@Override
		public void validate(PersonIdentification object, List<ValidationMessage> resultList) {
			// nothing to validate
		}
	}
	
}
