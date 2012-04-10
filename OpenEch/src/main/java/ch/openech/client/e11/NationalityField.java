package ch.openech.client.e11;

import java.util.ArrayList;
import java.util.List;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.person.Nationality;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.xml.read.StaxEch0072;

public class NationalityField extends ObjectField<Nationality> {
	private final ComboBox<Nationality> comboBox;
	private final Nationality itemUnknownNationality = Nationality.newUnknown();
	private final Nationality itemWithoutNationality = Nationality.newWithout();
	
	public NationalityField(Object key) {
		super(key);
		
		comboBox = ClientToolkit.getToolkit().createComboBox(listener());

		List<CountryIdentification> countries = StaxEch0072.getInstance().getCountryIdentifications();
		
		List<Nationality> items = new ArrayList<Nationality>(countries.size() + 2);
		items.add(itemUnknownNationality);
		items.add(itemWithoutNationality);

		for (CountryIdentification country : countries) {
			Nationality nationality = new Nationality();
			country.copyTo(nationality.nationalityCountry);
			items.add(nationality);
		}
		comboBox.setObjects(items);
	}
	
	@Override
	public Object getComponent() {
		return comboBox;
	}
	
	@Override
	public void setObject(Nationality nationality) {
		if (nationality == null) {
			nationality = Nationality.newUnknown();
		}
		super.setObject(nationality);
	}
	
	@Override
	public Nationality getObject() {
		Nationality nationality = super.getObject();
		
		if (comboBox.getSelectedObject() != null) {
			comboBox.getSelectedObject().copyTo(nationality);
		} else {
			nationality.clear();
		}
		return nationality;
	}

	@Override
	protected void show(Nationality nationality) {
		String status = nationality.nationalityStatus;
		if ("2".equals(status)) {
			comboBox.setSelectedObject(nationality);
		} else if ("1".equals(status)) {
			comboBox.setSelectedObject(itemWithoutNationality);
		} else if ("0".equals(status)) {
			comboBox.setSelectedObject(itemUnknownNationality);
		} else if (status != null) {
			comboBox.setSelectedObject(nationality);
		}
		
		comboBox.setSelectedObject(nationality);
	}

}
