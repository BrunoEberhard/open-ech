package ch.openech.client.e11;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.person.Nationality;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.read.StaxEch0072;

public class NationalityField extends ObjectField<Nationality> {
	private static final Logger logger = Logger.getLogger(NationalityField.class.getName());
	
	private final ComboBox comboBox;
	private final List<CountryIdentification> countries;
	
	public NationalityField(Object key) {
		super(key);
		
		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		
		countries = StaxEch0072.getInstance().getCountryIdentifications();
		List<String> countryNames = new ArrayList<String>(countries.size() + 2);
		countryNames.add("Staatsangehörigkeit unbekannt");
		countryNames.add("Staatenlos");
		for (CountryIdentification country : countries) {
			countryNames.add(country.countryNameShort);
		}
		comboBox.setObjects(countryNames);
	}
	
	@Override
	public Object getComponent() {
		return comboBox;
	}
	
	@Override
	public void setObject(Nationality nationality) {
		if (nationality == null) {
			nationality = new Nationality();
			nationality.nationalityStatus = "0";
		}
		super.setObject(nationality);
	}
	
	@Override
	public Nationality getObject() {
		Nationality nationality = super.getObject();
		nationality.nationalityStatus = "0";
		nationality.nationalityCountry.clear();
		
		String value = (String)comboBox.getSelectedObject();
		if ("Staatenlos".equals(value)) {
			nationality.nationalityStatus = "1";
		} else if ("Staatsangehörigkeit unbekannt".equals(value)) {
			nationality.nationalityStatus = "0";
		} else if (value != null) {
			for (CountryIdentification country : countries) {
				if (StringUtils.equals(country.countryNameShort, value)) {
					nationality.nationalityStatus = "2";
					country.copyTo(nationality.nationalityCountry);
					break;
				}
			}
		}
		return nationality;
	}

	@Override
	protected void show(Nationality nationality) {
		String status = nationality.nationalityStatus;
		if ("2".equals(status)) {
			CountryIdentification countryIdentification = nationality.nationalityCountry;
			int index = countries.indexOf(countryIdentification);
			if (index < 0) {
				logger.warning("Unknown country");
				comboBox.setSelectedObject(null);
			} else {
				comboBox.setSelectedObject(countryIdentification.countryNameShort);
			}
		} else if ("1".equals(status)) {
			comboBox.setSelectedObject("Staatenlos");
		} else if ("0".equals(status)) {
			comboBox.setSelectedObject("Staatsangehörigkeit unbekannt");
		} else if (status != null) {
			logger.warning("Unknown nationalityStatus");
			comboBox.setSelectedObject(null);
		} else {
			logger.info("nationalityStatus is null");
			comboBox.setSelectedObject(null);
		}
	}

}
