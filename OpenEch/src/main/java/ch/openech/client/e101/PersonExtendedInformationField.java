package ch.openech.client.e101;

import ch.openech.dm.person.PersonExtendedInformation;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.fields.MultiLineObjectField;
import ch.openech.mj.edit.form.FormVisual;

public class PersonExtendedInformationField extends MultiLineObjectField<PersonExtendedInformation> {

	public PersonExtendedInformationField(Object key, boolean editable) {
		super(key, editable);
	}
	
	@Override
	protected void display(PersonExtendedInformation information) {
		if (information != null) {
			addHtml(information.toHtml());
		}
		if (isEditable()) {
			addAction(new EditorDialogAction(new ObjectFieldEditor()));
			addAction(new RemoveObjectAction());
		}
	}
	
	@Override
	public FormVisual<PersonExtendedInformation> createFormPanel() {
		return new PersonExtendedInformationPanel();
	}

}
