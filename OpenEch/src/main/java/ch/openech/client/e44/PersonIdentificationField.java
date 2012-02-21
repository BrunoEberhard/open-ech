package ch.openech.client.e44;

import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.MultiLineTextField;

public class PersonIdentificationField extends ObjectField<PersonIdentification> {
	protected final MultiLineTextField text;

	public PersonIdentificationField(Object key) {
		super(Constants.getConstant(key));
		
		text = ClientToolkit.getToolkit().createMultiLineTextField();
		
		// editable 
        addAction(new PersonSearchEditor());
        // EditObjectAction kann nicht verwendet werden, sondern die PersonIdentificationEditAction,
        // weil der Typ vom FormPanel PersonIdentification und nicht Person ist
        addAction(new PersonIdentificationEditor());
        addAction(new RemoveObjectAction());
	}
	
	@Override
	protected IComponent getComponent0() {
		return text;
	}

	@Override
	public FormVisual<PersonIdentification> createFormPanel() {
		// not used
		return null;
	}

	@Override
	protected void display(PersonIdentification object) {
		if (object != null) {
			text.setText(object.toHtml());
		} else {
			text.setText(null);
		}
	}

	public final class PersonSearchEditor extends Editor<Person> {
		
		@Override
		protected FormVisual<Person> createForm() {
			return new PersonSearchForm();
		}

		@Override
		protected Person load() {
			// not used
			return null;
		}

		@Override
		protected void validate(Person object, List<ValidationMessage> resultList) {
			// not used
		}

		@Override
		protected boolean save(Person person) {
			if (person != null) {
				PersonIdentificationField.this.setObject(person.personIdentification);
			}
			return true;
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
