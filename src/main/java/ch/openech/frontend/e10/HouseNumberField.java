package ch.openech.frontend.e10;

import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.ClientToolkit.InputType;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.EchFormats;
import ch.openech.model.common.HouseNumber;

public class HouseNumberField extends AbstractEditField<HouseNumber> {
	private HouseNumber houseNumber;
	private final TextField houseNumberTextField;
	private final TextField dwellingNumberTextField;
	private final IComponent horizontalLayout;
	
	public HouseNumberField(PropertyInterface property) {
		super(property, true);
		
		houseNumberTextField = ClientToolkit.getToolkit().createTextField(EchFormats.houseNumber, null, InputType.FREE, null, listener());
		dwellingNumberTextField = ClientToolkit.getToolkit().createTextField(EchFormats.dwellingNumber, null, InputType.FREE, null, listener());

		horizontalLayout = ClientToolkit.getToolkit().createComponentGroup(houseNumberTextField, dwellingNumberTextField);
	}
	
	@Override
	public IComponent getComponent() {
		return horizontalLayout;
	}

	@Override
	public HouseNumber getObject() {
		houseNumber.houseNumber = houseNumberTextField.getText();
		houseNumber.dwellingNumber = dwellingNumberTextField.getText();
		return houseNumber;
	}

	@Override
	public void setObject(HouseNumber houseNumber) {
		this.houseNumber = houseNumber != null ? houseNumber : new HouseNumber();
		update();
	}

	private void update() {
		houseNumberTextField.setText(houseNumber.houseNumber);
		dwellingNumberTextField.setText(houseNumber.dwellingNumber);
	}

}
