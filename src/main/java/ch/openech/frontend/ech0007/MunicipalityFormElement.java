package ch.openech.frontend.ech0007;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.repository.sql.EmptyObjects;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.Codes;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0071.Municipality;
import ch.openech.datagenerator.DataGenerator;

public class MunicipalityFormElement extends AbstractFormElement<Municipality> implements Mocking {
	private final List<Municipality> municipalities;
	private final Input<Municipality> comboBox;

	public MunicipalityFormElement(Municipality key, boolean allowFederalRegister) {
		this(Keys.getProperty(key), allowFederalRegister);
	}
	
	public MunicipalityFormElement(PropertyInterface property, boolean allowFederalRegister) {
		super(property);
		
		municipalities = Codes.get(Municipality.class).stream().filter(m -> m.municipalityAbolitionMode == null).collect(Collectors.toList());
		Collections.sort(municipalities);
		List<Municipality> items;
		if (!allowFederalRegister) {
			items = municipalities.stream().filter((municipalityIdentification) -> (municipalityIdentification.id > 0)).collect(Collectors.toList());
		} else {
			items = new ArrayList<>(municipalities);
		}
		Collections.sort(items);
		comboBox = Frontend.getInstance().createComboBox(items, listener());
	}
	
	@Override
	public IComponent getComponent() {
		return comboBox;
	}

	@Override
	public void setValue(Municipality object) {
		if (EmptyObjects.isEmpty(object)) { 
			object = null;
		}
		comboBox.setValue(object);
	}
	
	@Override
	public Municipality getValue() {
		Municipality municipality = new Municipality();
		if (comboBox.getValue() != null) {
			return CloneHelper.clone(comboBox.getValue());
		}
		return municipality;
	}
	
	// 

	@Override
	public void mock() {
		setValue(DataGenerator.municipality());
	}
}
