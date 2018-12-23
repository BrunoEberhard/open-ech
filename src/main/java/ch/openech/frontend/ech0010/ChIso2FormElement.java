package ch.openech.frontend.ech0010;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;

import ch.ech.ech0010.MailAddress;

public class ChIso2FormElement extends AbstractFormElement<String> {
	private final Input<String> textField;

	public ChIso2FormElement() {
		this(MailAddress.$.addressInformation.country.iso2Id);
	}
	
	public ChIso2FormElement(Object key) {
		super(key);

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