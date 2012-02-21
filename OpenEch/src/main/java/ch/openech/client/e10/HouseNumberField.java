package ch.openech.client.e10;

import ch.openech.dm.common.HouseNumber;
import ch.openech.mj.db.model.Formats;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.HorizontalLayout;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;

public class HouseNumberField extends AbstractEditField<HouseNumber> {
	private HouseNumber houseNumber;
	private final TextField houseNumberTextField;
	private final TextField dwellingNumberTextField;
	private final HorizontalLayout horizontalLayout;
	
	public HouseNumberField(Object key) {
		super(key);
		
		houseNumberTextField = ClientToolkit.getToolkit().createTextField(listener(), Formats.getInstance().getFormat(HouseNumber.class, HouseNumber.HOUSE_NUMBER.houseNumber).getSize());
		dwellingNumberTextField = ClientToolkit.getToolkit().createTextField(listener(), Formats.getInstance().getFormat(HouseNumber.class, HouseNumber.HOUSE_NUMBER.dwellingNumber).getSize());

		horizontalLayout = ClientToolkit.getToolkit().createHorizontalLayout(houseNumberTextField, dwellingNumberTextField);
	}
	
	@Override
	public IComponent getComponent0() {
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

	@Override
	protected Indicator[] getIndicatingComponents() {
		return new Indicator[]{houseNumberTextField};
	};
	
}