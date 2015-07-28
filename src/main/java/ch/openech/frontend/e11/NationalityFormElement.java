package ch.openech.frontend.e11;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.Codes;

import ch.openech.model.common.CountryIdentification;
import ch.openech.model.person.Nationality;

public class NationalityFormElement extends AbstractFormElement<Nationality> {
	private final Input<Nationality> comboBox;
	private final Nationality itemUnknownNationality = Nationality.newUnknown();
	private final Nationality itemWithoutNationality = Nationality.newWithout();
	
	public NationalityFormElement(PropertyInterface property) {
		super(property);
		
		List<CountryIdentification> countries = Codes.get(CountryIdentification.class);
		
		List<Nationality> items = new ArrayList<Nationality>(countries.size() + 2);
		items.add(itemUnknownNationality);
		items.add(itemWithoutNationality);

		for (CountryIdentification country : countries) {
			Nationality nationality = new Nationality(country);
			items.add(nationality);
		}

		comboBox = Frontend.getInstance().createComboBox(items, listener());
	}
	
	@Override
	public IComponent getComponent() {
		return comboBox;
	}
	
	@Override
	public void setValue(Nationality nationality) {
		comboBox.setValue(nationality);
	}
	
	@Override
	public Nationality getValue() {
		Nationality nationality = Nationality.newUnknown();
		
		if (comboBox.getValue() != null) {
			comboBox.getValue().copyTo(nationality);
		} else {
			nationality.clear();
		}
		return nationality;
	}

}
