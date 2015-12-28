package ch.openech.frontend.e11;

import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.mock.Mocking;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.model.common.DwellingAddress;
import ch.openech.xml.write.EchSchema;

public class DwellingAddressFormElement extends ObjectFormElement<DwellingAddress> implements Mocking {
	private final EchSchema echSchema;
	
	public DwellingAddressFormElement(PropertyInterface property, EchSchema echSchema, boolean editable) {
		super(property, editable);
		this.echSchema = echSchema;
	}
	
	@Override
	protected Action[] getActions() {
		return new Action[] { getEditorAction() };
	}
	
	@Override
	protected void show(DwellingAddress object) {
		if (isEditable()) {
			add(object, new RemoveObjectAction());
		} else {
			add(object);
		}
	}

	@Override
	protected DwellingAddress createObject() {
		DwellingAddress dwellingAddress = super.createObject();
		dwellingAddress.echSchema = echSchema;
		return dwellingAddress;
	}
	
	@Override
	public Form<DwellingAddress> createForm() {
		return new DwellingAddressPanel(echSchema);
	}
	
	//

	@Override
	public void mock() {
		DwellingAddress dwellingAddress = DataGenerator.dwellingAddress();
		if (echSchema.addressesAreBusiness()) {
			// Kollektivhaushalt oder ähnliches passt auch bei Unternehmen nicht wirklich
			dwellingAddress.typeOfHousehold = null;
		}
		setValue(dwellingAddress);
	}

}
