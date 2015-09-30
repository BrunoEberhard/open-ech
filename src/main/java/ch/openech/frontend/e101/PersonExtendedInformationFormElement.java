package ch.openech.frontend.e101;

import org.minimalj.backend.sql.EmptyObjects;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.person.PersonExtendedInformation;

public class PersonExtendedInformationFormElement extends ObjectFormElement<PersonExtendedInformation> {

	public PersonExtendedInformationFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}
	
	@Override
	protected void show(PersonExtendedInformation information) {
		if (!EmptyObjects.isEmpty(information)) {
			add(information, new RemoveObjectAction());
		}
	}
	
	@Override
	protected Action[] getActions() {
		return new Action[] { getEditorAction() };
	}
	
	@Override
	public Form<PersonExtendedInformation> createFormPanel() {
		return new PersonExtendedInformationPanel();
	}

}
