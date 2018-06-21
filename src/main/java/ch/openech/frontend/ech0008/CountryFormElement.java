package ch.openech.frontend.ech0008;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.Codes;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0008.Country;
import ch.ech.ech0072.CountryInformation;


public class CountryFormElement extends AbstractFormElement<Country> implements Mocking {
	private static final Logger logger = Logger.getLogger(CountryFormElement.class.getName());

	private final Input<CountryInformation> comboBox;
	private final List<CountryInformation> countries;
	
	public CountryFormElement(Object key) {
		this(Keys.getProperty(key));
	}
	
	public CountryFormElement(PropertyInterface property) {
		super(property);
		countries = Codes.get(CountryInformation.class);
		comboBox = Frontend.getInstance().createComboBox(countries, listener());
	}
	
	@Override
	public IComponent getComponent() {
		return comboBox;
	}
	
	@Override
	public Country getValue() {
		CountryInformation countryInformation = comboBox.getValue();
		if (countryInformation != null) {
			return countryInformation.getCountry();
		} else {
			return null;
		}
	}

	@Override
	public void setValue(Country country) {
		if (country != null) {
			Optional<CountryInformation> countryInformation = countries.stream().filter(ci -> ci.id.equals(country.countryId)).findFirst();
			comboBox.setValue(countryInformation.orElse(null));
		} else {
			comboBox.setValue(null);
		}
	}

	@Override
	public void mock() {
		if (!countries.isEmpty()) {
			int index = (int) (Math.random() * countries.size());
			comboBox.setValue(countries.get(index));
		}
	}

}
