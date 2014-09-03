package ch.openech.frontend.e11;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.ComboBox;
import org.minimalj.model.PropertyInterface;
import org.minimalj.util.Codes;

import ch.openech.model.common.CountryIdentification;
import ch.openech.model.person.Nationality;

public class NationalityField extends AbstractEditField<Nationality> {
	private final ComboBox<Nationality> comboBox;
	private final Nationality itemUnknownNationality = Nationality.newUnknown();
	private final Nationality itemWithoutNationality = Nationality.newWithout();
	
	public NationalityField(PropertyInterface property) {
		super(property, true);
		
		comboBox = ClientToolkit.getToolkit().createComboBox(listener());

		List<CountryIdentification> countries = Codes.get(CountryIdentification.class);
		
		List<Nationality> items = new ArrayList<Nationality>(countries.size() + 2);
		items.add(itemUnknownNationality);
		items.add(itemWithoutNationality);

		for (CountryIdentification country : countries) {
			Nationality nationality = new Nationality();
			nationality.nationalityCountry = country;
			items.add(nationality);
		}
		comboBox.setObjects(items);
	}
	
	@Override
	public IComponent getComponent() {
		return comboBox;
	}
	
	@Override
	public void setObject(Nationality nationality) {
//		String status = nationality != null ? nationality.nationalityStatus : "0";
//		if ("2".equals(status)) {
//			comboBox.setSelectedObject(nationality);
//		} else if ("1".equals(status)) {
//			comboBox.setSelectedObject(itemWithoutNationality);
//		} else if ("0".equals(status)) {
//			comboBox.setSelectedObject(itemUnknownNationality);
//		} else if (status != null) {
//			comboBox.setSelectedObject(nationality);
//		}
		comboBox.setSelectedObject(nationality);
	}
	
	@Override
	public Nationality getObject() {
		Nationality nationality = Nationality.newUnknown();
		
		if (comboBox.getSelectedObject() != null) {
			comboBox.getSelectedObject().copyTo(nationality);
		} else {
			nationality.clear();
		}
		return nationality;
	}

}
