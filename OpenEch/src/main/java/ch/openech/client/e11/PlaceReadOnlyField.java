package ch.openech.client.e11;

import ch.openech.dm.common.Place;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;


public class PlaceReadOnlyField implements FormField<Place> {
	private final TextField textField;
	private final PropertyInterface property;
	
	public PlaceReadOnlyField(PropertyInterface property) {
		this.property = property;
		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
	}

	@Override
	public IComponent getComponent() {
		return textField;
	}

	@Override
	public void setObject(Place value) {
		if (value.isSwiss()) {
			textField.setText(value.municipalityIdentification.toString());
		} else if (value.isForeign()) {
			String text = value.countryIdentification.toString();
			if (value.foreignTown != null) {
				text = text + ", " + value.foreignTown;
			}
			textField.setText(text);
		} else {
			textField.setText("-");
		}
	}
	
	public void setEnabled(boolean enabled) {
		textField.setEnabled(enabled);
	}

	@Override
	public PropertyInterface getProperty() {
		return property;
	}

}
