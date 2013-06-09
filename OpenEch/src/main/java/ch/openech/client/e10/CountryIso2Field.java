package ch.openech.client.e10;

import java.util.List;

import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.xml.read.StaxEch0072;

public class CountryIso2Field extends AbstractEditField<String> {
	private final ComboBox<String> comboBox;
	
	private final List<String> countryNames;

	public CountryIso2Field(PropertyInterface property) {
		super(property, true);

		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		countryNames = StaxEch0072.getInstance().getCountryIdISO2s();
		comboBox.setObjects(countryNames);
	}
	
	@Override
	public IComponent getComponent() {
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
	
}
