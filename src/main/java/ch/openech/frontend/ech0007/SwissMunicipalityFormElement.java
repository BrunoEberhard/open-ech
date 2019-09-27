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
import org.minimalj.model.ViewUtil;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.Codes;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0007.SwissMunicipality;
import ch.ech.ech0071.Municipality;
import ch.openech.datagenerator.DataGenerator;

public class SwissMunicipalityFormElement extends AbstractFormElement<SwissMunicipality> implements Mocking {
	private final List<Municipality> municipalities;
	private final Input<Municipality> comboBox;

	public SwissMunicipalityFormElement(SwissMunicipality key) {
		this(Keys.getProperty(key));
	}
	
	public SwissMunicipalityFormElement(PropertyInterface property) {
		super(property);
		
		municipalities = Codes.get(Municipality.class).stream().filter(m -> m.municipalityAbolitionMode == null).collect(Collectors.toList());
		Collections.sort(municipalities);
		List<Municipality> items = new ArrayList<>(municipalities);
		Collections.sort(items);
		comboBox = Frontend.getInstance().createComboBox(items, listener());
	}
	
	@Override
	public IComponent getComponent() {
		return comboBox;
	}

	@Override
	public void setValue(SwissMunicipality object) {
		Municipality municipality = ViewUtil.viewed(object);
		comboBox.setValue(municipality);
	}
	
	@Override
	public SwissMunicipality getValue() {
		Municipality municipality = comboBox.getValue();
		if (municipality != null) {
			return ViewUtil.view(municipality, new SwissMunicipality());
		} else {
			return null;
		}
	}
	
	// 

	@Override
	public void mock() {
		comboBox.setValue(DataGenerator.municipality());
	}
}
