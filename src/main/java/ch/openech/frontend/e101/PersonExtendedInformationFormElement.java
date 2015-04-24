package ch.openech.frontend.e101;

import org.minimalj.backend.db.EmptyObjects;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectPanelFormElement;
import org.minimalj.model.properties.PropertyInterface;

import  ch.openech.model.person.PersonExtendedInformation;

public class PersonExtendedInformationFormElement extends ObjectPanelFormElement<PersonExtendedInformation> {

	public PersonExtendedInformationFormElement(PropertyInterface property, boolean editable) {
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
