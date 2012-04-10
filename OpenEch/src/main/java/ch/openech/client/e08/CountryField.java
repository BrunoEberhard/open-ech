package ch.openech.client.e08;

import java.util.logging.Logger;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.xml.read.StaxEch0072;

/* Dieses Feld wurde erst mit ech 112 gebraucht. 
 * 
 */
public class CountryField extends ObjectField<CountryIdentification> implements DemoEnabled {
	private static final Logger logger = Logger.getLogger(CountryField.class.getName());

	private final ComboBox<CountryIdentification> comboBox;
	
	public CountryField(String name) {
		super(name);
		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		comboBox.setObjects(StaxEch0072.getInstance().getCountryIdentifications());
	}
	
	@Override
	public Object getComponent() {
		return comboBox;
	}
	
	//

	@Override
	public void setObject(CountryIdentification countryIdentification) {
		if (countryIdentification == null) {
			countryIdentification = new CountryIdentification();
		}
		super.setObject(countryIdentification);
	}

	@Override
	public void show(CountryIdentification value) {
		int index = StaxEch0072.getInstance().getCountryIdentifications().indexOf(value);
		if (index >= 0) {
			comboBox.setSelectedObject(value);
		} else if (!value.isEmpty()){
			logger.warning("Unknown country");
			comboBox.setSelectedObject(null);
		} else {
			comboBox.setSelectedObject(null);
		}
	}

	@Override
	public void fillWithDemoData() {
		int index = (int) (Math.random() * (double) StaxEch0072.getInstance().getCountryIdentifications().size());
		CountryIdentification country =  StaxEch0072.getInstance().getCountryIdentifications().get(index);
		setObject(CloneHelper.cloneIfPossible(country));
	}

}
