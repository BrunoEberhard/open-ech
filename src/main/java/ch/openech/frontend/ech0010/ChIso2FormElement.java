package ch.openech.frontend.ech0010;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;

import ch.ech.ech0008.Country;

public class ChIso2FormElement extends AbstractFormElement<Country> {
	private final Input<String> textField;
	private static final Country switzerland = new Country();

	static {
		switzerland.id = 8100;
		switzerland.shortNameDe = "Schweiz";
		switzerland.iso2Id = "CH";
	}

	public ChIso2FormElement(Country key) {
		super(key);

		textField = Frontend.getInstance().createReadOnlyTextField();
		textField.setValue("CH");
	}

	@Override
	public IComponent getComponent() {
		return textField;
	}

	@Override
	public Country getValue() {
		return switzerland;
	}

	@Override
	public void setValue(Country object) {
		// not supported
	}

}