package ch.openech.frontend.e101;

import org.minimalj.backend.db.EmptyObjects;
import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.PropertyInterface;

import  ch.openech.model.person.PersonExtendedInformation;

public class PersonExtendedInformationField extends ObjectFlowField<PersonExtendedInformation> {

	public PersonExtendedInformationField(PropertyInterface property, boolean editable) {
		super(property, editable);
	}
	
	@Override
	protected void show(PersonExtendedInformation information) {
		if (!EmptyObjects.isEmpty(information)) {
			addText(information.toHtml());
		}
	}

	@Override
	protected void showActions() {
		addAction(new ObjectFieldEditor());
		addAction(new RemoveObjectAction());
	}
	
	@Override
	public Form<PersonExtendedInformation> createFormPanel() {
		return new PersonExtendedInformationPanel();
	}

}
