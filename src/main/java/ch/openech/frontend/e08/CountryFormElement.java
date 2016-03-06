package ch.openech.frontend.e08;

import java.util.List;
import java.util.logging.Logger;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.Codes;
import org.minimalj.util.mock.Mocking;

import ch.openech.model.common.CountryIdentification;

/* Dieses Feld wurde erst mit ech 112 gebraucht. 
 * 
 */
public class CountryFormElement extends AbstractFormElement<CountryIdentification> implements Mocking {
	private static final Logger logger = Logger.getLogger(CountryFormElement.class.getName());

	private final Input<CountryIdentification> comboBox;
	private final List<CountryIdentification> countries;
	
	public CountryFormElement(Object key) {
		this(Keys.getProperty(key));
	}
	
	public CountryFormElement(PropertyInterface property) {
		super(property);
		countries = Codes.get(CountryIdentification.class);
		comboBox = Frontend.getInstance().createComboBox(countries, listener());
	}
	
	@Override
	public IComponent getComponent() {
		return comboBox;
	}
	
	@Override
	public CountryIdentification getValue() {
		return comboBox.getValue();
	}

	@Override
	public void setValue(CountryIdentification country) {
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
		if (!countries.isEmpty()) {
			int index = (int) (Math.random() * countries.size());
			setValue(countries.get(index));
		}
	}

}
