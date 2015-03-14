package ch.openech.frontend.e11;

import org.minimalj.frontend.edit.fields.FormField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.properties.PropertyInterface;

import ch.openech.model.common.Place;


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
		if (value != null && value.isSwiss()) {
			textField.setText(value.municipalityIdentification != null ? value.municipalityIdentification.toString() : "-");
		} else if (value != null && value.isForeign()) {
			String text = value.countryIdentification != null ? value.countryIdentification.toString() : "";
			if (value.foreignTown != null) {
				text = text + ", " + value.foreignTown;
			}
			textField.setText(text);
		} else {
			textField.setText("-");
		}
	}
	
	@Override
	public PropertyInterface getProperty() {
		return property;
	}
}
