package ch.openech.frontend.e10;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.InputType;
import org.minimalj.frontend.form.element.AbstractFormElement;
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
		
		houseNumberTextField = Frontend.getInstance().createTextField(EchFormats.houseNumber, null, InputType.FREE, null, listener());
		dwellingNumberTextField = Frontend.getInstance().createTextField(EchFormats.dwellingNumber, null, InputType.FREE, null, listener());

		horizontalLayout = Frontend.getInstance().createComponentGroup(houseNumberTextField, dwellingNumberTextField);
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
