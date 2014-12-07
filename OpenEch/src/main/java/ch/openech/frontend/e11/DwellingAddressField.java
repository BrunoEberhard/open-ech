package ch.openech.frontend.e11;

import org.minimalj.frontend.edit.fields.ObjectFlowField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.DemoEnabled;

import ch.openech.datagenerator.DataGenerator;
import  ch.openech.model.common.DwellingAddress;
import ch.openech.xml.write.EchSchema;

public class DwellingAddressField extends ObjectFlowField<DwellingAddress> implements DemoEnabled {
	private final EchSchema echSchema;
	
	public DwellingAddressField(PropertyInterface property, EchSchema echSchema, boolean editable) {
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
	public void fillWithDemoData() {
		DwellingAddress dwellingAddress = DataGenerator.dwellingAddress();
		if (echSchema.addressesAreBusiness()) {
			// Kollektivhaushalt oder ähnliches passt auch bei Unternehmen nicht wirklich
			dwellingAddress.typeOfHousehold = null;
		}
		setObject(dwellingAddress);
	}

	@Override
	public void setObject(DwellingAddress object) {
		// TODO Auto-generated method stub
		super.setObject(object);
	}
	
}
