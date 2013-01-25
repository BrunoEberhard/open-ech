package ch.openech.client.e08;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.mj.edit.fields.FormField;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;

/* Dieses Feld wurde erst mit ech 112 gebraucht. Dazu wurde 
 * das PlaceField eingedampft (alles was mit Municipality zu tun hatte entfernt).
 */
public class CountryReadOnlyField implements FormField<CountryIdentification> {
	private final TextField textCountry;
	private final PropertyInterface property;
	
	public CountryReadOnlyField(PropertyInterface property) {
		this.property = property;
		textCountry = ClientToolkit.getToolkit().createReadOnlyTextField();
	}
	
	@Override
	public IComponent getComponent() {
		return textCountry;
	}
	
	@Override
	public void setObject(CountryIdentification country) {
		textCountry.setText(country != null ? country.countryNameShort : "");
	}

	@Override
	public PropertyInterface getProperty() {
		return property;
	}

}
