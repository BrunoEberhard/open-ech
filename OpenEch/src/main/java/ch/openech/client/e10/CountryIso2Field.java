package ch.openech.client.e10;

import java.util.List;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.xml.read.StaxEch0072;

public class CountryIso2Field extends AbstractEditField<String> implements ChangeListener {
	private static final Logger logger = Logger.getLogger(CountryIso2Field.class.getName());
	private final ComboBox<String> comboBox;
	
	private final List<String> countryNames;

	public CountryIso2Field(Object key) {
		super(key, true);

		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		countryNames = StaxEch0072.getInstance().getCountryIdISO2s();
		comboBox.setObjects(countryNames);
	}
	
	@Override
	public Object getComponent() {
		return comboBox;
	}

	@Override
	public String getObject() {
		return (String) comboBox.getSelectedObject();
	}

	@Override
	public void setObject(String value) {
		comboBox.setSelectedObject(value);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		fireChange();
	}
	
}
