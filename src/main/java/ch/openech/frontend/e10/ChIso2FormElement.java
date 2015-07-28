package ch.openech.frontend.e10;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;

import ch.openech.model.common.Address;

public class ChIso2FormElement extends AbstractFormElement<String> {
	private final Input<String> textField;

	public ChIso2FormElement() {
		super(Address.$.country);

		textField = Frontend.getInstance().createReadOnlyTextField();
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
