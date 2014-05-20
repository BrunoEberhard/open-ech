package ch.openech.frontend.e10;

import java.util.List;

import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ComboBox;
import org.minimalj.frontend.toolkit.IComponent;
import org.minimalj.model.PropertyInterface;

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
