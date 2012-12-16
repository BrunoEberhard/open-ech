package ch.openech.client.e10;

import ch.openech.dm.EchFormats;
import ch.openech.dm.common.HouseNumber;
import ch.openech.mj.db.model.PropertyInterface;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.HorizontalLayout;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;

public class HouseNumberField extends AbstractEditField<HouseNumber> {
	private HouseNumber houseNumber;
	private final TextField houseNumberTextField;
	private final TextField dwellingNumberTextField;
	private final HorizontalLayout horizontalLayout;
	
	public HouseNumberField(PropertyInterface property) {
		super(property, true);
		
		houseNumberTextField = ClientToolkit.getToolkit().createTextField(listener(), EchFormats.houseNumber);
		dwellingNumberTextField = ClientToolkit.getToolkit().createTextField(listener(), EchFormats.dwellingNumber);

		horizontalLayout = ClientToolkit.getToolkit().createHorizontalLayout(houseNumberTextField, dwellingNumberTextField);
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
