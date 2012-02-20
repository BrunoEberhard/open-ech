package ch.openech.client.e11;

import ch.openech.dm.common.Place;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;


public class PlaceReadOnlyField extends ObjectField<Place> {
	private TextField text;

	public PlaceReadOnlyField(Object key) {
		super(key);
		
		text = ClientToolkit.getToolkit().createReadOnlyTextField();
	}

	@Override
	protected IComponent getComponent0() {
		return text;
	}

	@Override
	public void setObject(Place value) {
		if (value == null) {
			value = new Place();
		}
		super.setObject(value);
	}
	
	public void setEnabled(boolean enabled) {
		text.setEnabled(enabled);
	}
	
	@Override
	public void display(Place value) {
		if (value.isSwiss()) {
			text.setText(value.municipalityIdentification.toString());
		} else if (value.isForeign()) {
			text.setText(value.countryIdentification.toString());
		} else {
			text.setText("-");
		}
	}

	@Override
	protected FormVisual<Place> createFormPanel() {
		// not used
		return null;
	}

}
