package ch.openech.client.e11;

import ch.openech.dm.common.Place;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IComponentDelegate;
import ch.openech.mj.toolkit.TextField;


public class PlaceReadOnlyField implements FormField<Place>, IComponentDelegate {
	private final TextField text;
	private final String name;
	
	public PlaceReadOnlyField(Object key) {
		this.name = Constants.getConstant(key);
		text = ClientToolkit.getToolkit().createReadOnlyTextField();
	}

	@Override
	public IComponent getComponent() {
		return text;
	}

	@Override
	public void setObject(Place value) {
		if (value.isSwiss()) {
			text.setText(value.municipalityIdentification.toString());
		} else if (value.isForeign()) {
			text.setText(value.countryIdentification.toString());
		} else {
			text.setText("-");
		}
	}
	
	public void setEnabled(boolean enabled) {
		text.setEnabled(enabled);
	}

	@Override
	public String getName() {
		return name;
	}

}
