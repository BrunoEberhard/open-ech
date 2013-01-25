package ch.openech.client.e11;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.EditorDialogAction;
import ch.openech.mj.edit.fields.ObjectFlowField;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.xml.write.EchSchema;

public class DwellingAddressField extends ObjectFlowField<DwellingAddress> implements DemoEnabled {
	private final EchSchema echSchema;
	
	public DwellingAddressField(PropertyInterface property, EchSchema echSchema, boolean editable) {
		super(property, editable);
		this.echSchema = echSchema;
	}
	
	@Override
	protected void show(DwellingAddress dwellingAddress) {
		addHtml(dwellingAddress.toHtml());
	}
	
	@Override
	protected void showActions() {
		addAction(new EditorDialogAction(new ObjectFieldEditor()));
		addAction(new RemoveObjectAction());
	}
	
	@Override
	public IForm<DwellingAddress> createFormPanel() {
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
