package ch.openech.client.e44;

import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.MultiLineTextField;

public class PersonField extends ObjectField<Person> {
	protected final MultiLineTextField text;

	public PersonField(Object key) {
		super(key);
		text = ClientToolkit.getToolkit().createMultiLineTextField();
		
		// editable 
        addAction(new PersonSearchEditor());
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
				PersonField.this.setObject(person);
				fireChange();
			}
			return true;
		}
	}
}
