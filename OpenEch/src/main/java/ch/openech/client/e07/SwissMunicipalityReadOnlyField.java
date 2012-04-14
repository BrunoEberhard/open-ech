package ch.openech.client.e07;

import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponentDelegate;
import ch.openech.mj.toolkit.TextField;

public class SwissMunicipalityReadOnlyField implements IComponentDelegate, FormField<MunicipalityIdentification> {
	private final String name;
	private final TextField textField;

	public SwissMunicipalityReadOnlyField(Object key) {
		this.name = Constants.getConstant(key);
		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Object getComponent() {
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
