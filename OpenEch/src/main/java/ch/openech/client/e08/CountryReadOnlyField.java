package ch.openech.client.e08;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.TextField;

/* Dieses Feld wurde erst mit ech 112 gebraucht. Dazu wurde 
 * das PlaceField eingedampft (alles was mit Municipality zu tun hatte entfernt).
 */
public class CountryReadOnlyField extends ObjectField<CountryIdentification> {
	private final TextField textCountry;
	
	public CountryReadOnlyField(String name) {
		super(name);
		textCountry = ClientToolkit.getToolkit().createReadOnlyTextField();
	}
	
	@Override
	public Object getComponent() {
		return decorateWithContextActions(textCountry);
	}
	
	//

	@Override
	public void display(CountryIdentification value) {
		textCountry.setText(value.countryNameShort);
	}

	@Override
	public FormVisual<CountryIdentification> createFormPanel() {
		return new CountryFreePanel();
	}

}
