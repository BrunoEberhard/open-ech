package ch.openech.frontend.ech0008;

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

import ch.ech.ech0072.v1.Country;

public class CountryFormElement extends AbstractFormElement<Country> implements Mocking {
	private static final Logger logger = Logger.getLogger(CountryFormElement.class.getName());

	private final Input<Country> comboBox;
	private final List<Country> countries;
	
	public CountryFormElement(Object key) {
		this(Keys.getProperty(key));
	}
	
	public CountryFormElement(PropertyInterface property) {
		super(property);
		countries = Codes.get(Country.class);
		comboBox = Frontend.getInstance().createComboBox(countries, listener());
	}
	
	@Override
	public IComponent getComponent() {
		return comboBox;
	}
	
	@Override
	public Country getValue() {
		return comboBox.getValue();
	}

	@Override
	public void setValue(Country country) {
		if (country != null) {
			int index = countries.indexOf(country);
			if (index >= 0) {
				comboBox.setValue(country);
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
