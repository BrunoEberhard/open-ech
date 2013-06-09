package ch.openech.client.e10;

import ch.openech.dm.common.Address;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;

public class ChIso2Field extends AbstractEditField<String> {
	private final TextField textField;

	public ChIso2Field() {
		super(Address.ADDRESS.country, true);

		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		textField.setText("CH");
	}

	@Override
	public IComponent getComponent() {
		return textField;
	}

	@Override
	public String getObject() {
		return "CH";
	}

	@Override
	public void setObject(String object) {
		// not supported
	}

}
