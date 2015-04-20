package ch.openech.frontend.e08;

import java.util.List;
import java.util.logging.Logger;

import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.ClientToolkit.Input;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.Codes;
import org.minimalj.util.mock.Mocking;

import ch.openech.model.common.CountryIdentification;

/* Dieses Feld wurde erst mit ech 112 gebraucht. 
 * 
 */
public class CountryField extends AbstractEditField<CountryIdentification> implements Mocking {
	private static final Logger logger = Logger.getLogger(CountryField.class.getName());

	private final Input<CountryIdentification> comboBox;
	private final List<CountryIdentification> countries;
	
	public CountryField(PropertyInterface property) {
		super(property, true);
		countries = Codes.get(CountryIdentification.class);
		comboBox = ClientToolkit.getToolkit().createComboBox(countries, listener());
	}
	
	@Override
	public IComponent getComponent() {
		return comboBox;
	}
	
	@Override
	public CountryIdentification getObject() {
		return comboBox.getValue();
	}

	@Override
	public void setObject(CountryIdentification country) {
		if (country != null) {
			int index = countries.indexOf(country);
			if (index >= 0) {
				comboBox.setValue(country);
			} else if (!country.isEmpty()){
				logger.warning("Unknown country");
				comboBox.setValue(null);
			} else {
				comboBox.setValue(null);
			}
		} else {
			comboBox.setValue(null);
		}
	}

	@Override
	public void mock() {
		int index = (int) (Math.random() * (double) countries.size());
		setObject(countries.get(index));
	}

}
