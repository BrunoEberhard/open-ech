package ch.openech.frontend.e10;

import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.ClientToolkit.Input;
import org.minimalj.frontend.toolkit.ClientToolkit.InputType;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.EchFormats;
import ch.openech.model.common.HouseNumber;

public class HouseNumberFormElement extends AbstractFormElement<HouseNumber> {
	private HouseNumber houseNumber;
	private final Input<String> houseNumberTextField;
	private final Input<String> dwellingNumberTextField;
	private final IComponent horizontalLayout;
	
	public HouseNumberFormElement(PropertyInterface property) {
		super(property);
		
		houseNumberTextField = ClientToolkit.getToolkit().createTextField(EchFormats.houseNumber, null, InputType.FREE, null, listener());
		dwellingNumberTextField = ClientToolkit.getToolkit().createTextField(EchFormats.dwellingNumber, null, InputType.FREE, null, listener());

		horizontalLayout = ClientToolkit.getToolkit().createComponentGroup(houseNumberTextField, dwellingNumberTextField);
	}
	
	@Override
	public IComponent getComponent() {
		return horizontalLayout;
	}

	@Override
	public HouseNumber getValue() {
		houseNumber.houseNumber = houseNumberTextField.getValue();
		houseNumber.dwellingNumber = dwellingNumberTextField.getValue();
		return houseNumber;
	}

	@Override
	public void setValue(HouseNumber houseNumber) {
		this.houseNumber = houseNumber != null ? houseNumber : new HouseNumber();
		update();
	}

	private void update() {
		houseNumberTextField.setValue(houseNumber.houseNumber);
		dwellingNumberTextField.setValue(houseNumber.dwellingNumber);
	}

}
