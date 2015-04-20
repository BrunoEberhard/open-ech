package ch.openech.frontend.e08;

import org.minimalj.frontend.edit.fields.FormField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.properties.PropertyInterface;

import  ch.openech.model.common.CountryIdentification;

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
		textCountry.setValue(country != null ? country.countryNameShort : "");
	}

	@Override
	public PropertyInterface getProperty() {
		return property;
	}

}
