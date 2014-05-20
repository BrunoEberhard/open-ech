package ch.openech.frontend.e07;

import org.minimalj.frontend.edit.fields.FormField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.IComponent;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.PropertyInterface;

import  ch.openech.model.common.MunicipalityIdentification;

public class MunicipalityReadOnlyField implements FormField<MunicipalityIdentification> {
	private final PropertyInterface property;
	private final TextField textField;

	public MunicipalityReadOnlyField(PropertyInterface property) {
		this.property = property;
		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
	}
	
	@Override
	public PropertyInterface getProperty() {
		return property;
	}
	
	@Override
	public IComponent getComponent() {
		return textField;
	}

	@Override
	public void setObject(MunicipalityIdentification object) {
		if (object != null) {
			textField.setText(object.toString());
		} else {
			textField.setText(null);
		}
	}
	
}
