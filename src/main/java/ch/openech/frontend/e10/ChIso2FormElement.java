package ch.openech.frontend.e10;

import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.ClientToolkit.Input;

import ch.openech.model.common.Address;

public class ChIso2FormElement extends AbstractFormElement<String> {
	private final Input<String> textField;

	public ChIso2FormElement() {
		super(Address.$.country);

		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		textField.setValue("CH");
	}

	@Override
	public IComponent getComponent() {
		return textField;
	}

	@Override
	public String getValue() {
		return "CH";
	}

	@Override
	public void setValue(String object) {
		// not supported
	}

}
