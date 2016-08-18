package ch.openech.frontend.e07;

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
import org.minimalj.persistence.sql.EmptyObjects;
import org.minimalj.util.Codes;
import org.minimalj.util.mock.Mocking;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.model.common.MunicipalityIdentification;

public class MunicipalityFormElement extends AbstractFormElement<MunicipalityIdentification> implements Mocking {
	private final List<MunicipalityIdentification> municipalities;
	private final Input<MunicipalityIdentification> comboBox;

	public MunicipalityFormElement(MunicipalityIdentification key, boolean allowFederalRegister) {
		this(Keys.getProperty(key), allowFederalRegister);
	}
	
	public MunicipalityFormElement(PropertyInterface property, boolean allowFederalRegister) {
		super(property);
		
		municipalities = Codes.get(MunicipalityIdentification.class);
		Collections.sort(municipalities);
		List<MunicipalityIdentification> items;
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
	public void setValue(MunicipalityIdentification object) {
		if (EmptyObjects.isEmpty(object)) { 
			object = null;
		}
		comboBox.setValue(object);
	}
	
	@Override
	public MunicipalityIdentification getValue() {
		MunicipalityIdentification municipality = new MunicipalityIdentification();
		if (comboBox.getValue() != null) {
			comboBox.getValue().copyTo(municipality);
		}
		return municipality;
	}
	
	// 

	@Override
	public void mock() {
		setValue(DataGenerator.municipalityIdentification());
	}
}
