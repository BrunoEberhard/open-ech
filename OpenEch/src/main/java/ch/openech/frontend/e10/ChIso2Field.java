package ch.openech.frontend.e10;

import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.TextField;

import  ch.openech.model.common.Address;

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
