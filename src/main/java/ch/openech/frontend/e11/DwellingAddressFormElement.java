package ch.openech.frontend.e11;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.ObjectPanelFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.mock.Mocking;

import ch.openech.datagenerator.DataGenerator;
import  ch.openech.model.common.DwellingAddress;
import ch.openech.xml.write.EchSchema;

public class DwellingAddressFormElement extends ObjectPanelFormElement<DwellingAddress> implements Mocking {
	private final EchSchema echSchema;
	
	public DwellingAddressFormElement(PropertyInterface property, EchSchema echSchema, boolean editable) {
		super(property, editable);
		this.echSchema = echSchema;
	}
	
	@Override
	protected void show(DwellingAddress dwellingAddress) {
		addText(dwellingAddress.toHtml());
	}
	
	@Override
	protected void showActions() {
		addAction(new ObjectFieldEditor());
		addAction(new RemoveObjectAction());
	}
	
	@Override
	public Form<DwellingAddress> createFormPanel() {
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

	@Override
	public void setValue(DwellingAddress object) {
		// TODO Auto-generated method stub
		super.setValue(object);
	}
	
}
