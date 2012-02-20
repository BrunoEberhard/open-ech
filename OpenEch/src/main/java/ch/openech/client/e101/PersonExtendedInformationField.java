package ch.openech.client.e101;

import ch.openech.dm.person.PersonExtendedInformation;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.MultiLineTextField;

public class PersonExtendedInformationField extends ObjectField<PersonExtendedInformation> {
	private MultiLineTextField text;

	public PersonExtendedInformationField(Object key, boolean editable) {
		super(key);
		
		text = ClientToolkit.getToolkit().createMultiLineTextField();
		if (editable) {
			addAction(new ObjectFieldEditor());
			addAction(new RemoveObjectAction());
		}
		// add(new SizedScrollPane(text, 7, 12));
	}
	
	@Override
	protected IComponent getComponent0() {
		return text;
	}

	@Override
	protected void display(PersonExtendedInformation information) {
		StringBuilder s = new StringBuilder();
		if (information != null) {
			information.toHtml(s);
		}
		text.setText(s.toString());
	}
	
	@Override
	public FormVisual<PersonExtendedInformation> createFormPanel() {
		return new PersonExtendedInformationPanel();
	}

}
