package ch.openech.frontend.ech0010;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.AnnotationUtil;

import ch.ech.ech0010.AddressInformation;
import ch.ech.ech0010.HouseNumber;

public class HouseNumberFormElement extends AbstractFormElement<HouseNumber> {
	private HouseNumber houseNumber;
	private final Input<String> houseNumberTextField;
	private final Input<String> dwellingNumberTextField;
	private final IComponent horizontalLayout;
	
	public HouseNumberFormElement(HouseNumber houseNumber, boolean editable) {
		super(houseNumber);
		
		if (editable) {
			int houseNumberSize = AnnotationUtil.getSize(Keys.getProperty(AddressInformation.$.houseNumber.houseNumber));
			int dwellingNumberSize = AnnotationUtil.getSize(Keys.getProperty(AddressInformation.$.houseNumber.dwellingNumber));

			houseNumberTextField = Frontend.getInstance().createTextField(houseNumberSize, null, null, listener());
			dwellingNumberTextField = Frontend.getInstance().createTextField(dwellingNumberSize, null, null, listener());
		} else {
			houseNumberTextField = Frontend.getInstance().createReadOnlyTextField();
			dwellingNumberTextField = Frontend.getInstance().createReadOnlyTextField();
		}

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
