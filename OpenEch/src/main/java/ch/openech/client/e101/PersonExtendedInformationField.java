package ch.openech.client.e101;

import ch.openech.dm.person.PersonExtendedInformation;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.PropertyInterface;

public class PersonExtendedInformationField extends ObjectFlowField<PersonExtendedInformation> {

	public PersonExtendedInformationField(PropertyInterface property, boolean editable) {
		super(property, editable);
	}
	
	@Override
	protected void show(PersonExtendedInformation information) {
		if (!information.isEmpty()) {
			addHtml(information.toHtml());
		}
	}

	@Override
	protected void showActions() {
		addAction(new EditorDialogAction(new ObjectFieldEditor()));
		addAction(new RemoveObjectAction());
	}
	
	@Override
	public IForm<PersonExtendedInformation> createFormPanel() {
		return new PersonExtendedInformationPanel();
	}

}
