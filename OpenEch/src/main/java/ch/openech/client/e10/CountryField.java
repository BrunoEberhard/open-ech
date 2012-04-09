package ch.openech.client.e10;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.xml.read.StaxEch0072;

public class CountryField extends AbstractEditField<String> implements ChangeListener {
	private static final Logger logger = Logger.getLogger(CountryField.class.getName());
	private final ComboBox comboBox;
	
	private final List<CountryIdentification> countries;
	private final List<String> countryNames;
	private ZipTownField connectedZipTownField;

	public CountryField() {
		this(null);
	}
		
	public CountryField(Object key) {
		super(key, true);

		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		countries = StaxEch0072.getInstance().getCountryIdentifications();
		countryNames = new ArrayList<String>();
		for (CountryIdentification country : countries) {
			if (country.countryIdISO2 != null) {
				countryNames.add(country.countryIdISO2);
			}
		}
		comboBox.setObjects(countryNames);
	}
	
		@Override
	public Object getComponent() {
		return comboBox;
	}

	
	public void setConnectedZipTownField(ZipTownField connectedZipTownField) {
		this.connectedZipTownField = connectedZipTownField;
	}
	
	@Override
    public void stateChanged(ChangeEvent e) {
		updateZipTowField();
		fireChange();
	}
    
	private void updateZipTowField() {
		if (connectedZipTownField != null) {
			String country = getObject();
			boolean foreign = !"CH".equals(country) && !"LI".equals(country);
			connectedZipTownField.setForeign(foreign);
		}
	}
	
	@Override
	public String getObject() {
		return (String) comboBox.getSelectedObject();
	}

	@Override
	public void setObject(String value) {
		int index = countryNames.indexOf(value);
		if (index < 0) {
			logger.warning("Unknown country");
			comboBox.setSelectedObject(null);
		} else {
			comboBox.setSelectedObject(value);
		}
	}
	
}
