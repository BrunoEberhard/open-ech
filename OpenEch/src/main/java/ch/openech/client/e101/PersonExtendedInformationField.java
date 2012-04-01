package ch.openech.client.e101;

import ch.openech.dm.person.PersonExtendedInformation;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.FormVisual;

public class PersonExtendedInformationField extends ObjectFlowField<PersonExtendedInformation> {

	public PersonExtendedInformationField(Object key, boolean editable) {
		super(key, editable);
	}
	
	@Override
	protected void show(PersonExtendedInformation information) {
		addHtml(information.toHtml());
	}

	@Override
	protected void showActions() {
		addAction(new EditorDialogAction(new ObjectFieldEditor()));
		addAction(new RemoveObjectAction());
	}
	
	@Override
	public FormVisual<PersonExtendedInformation> createFormPanel() {
		return new PersonExtendedInformationPanel();
	}

}
