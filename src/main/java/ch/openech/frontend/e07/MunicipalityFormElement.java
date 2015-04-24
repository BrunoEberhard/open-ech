package ch.openech.frontend.e07;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.ClientToolkit.Input;
import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.Codes;
import org.minimalj.util.mock.Mocking;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.model.code.FederalRegister;
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
		List<MunicipalityIdentification> items = new ArrayList<MunicipalityIdentification>(municipalities.size() + 5);
		if (allowFederalRegister) {
			for (FederalRegister federalRegister : FederalRegister.values()) {
				items.add(new FederalRegisterMunicipality(federalRegister));
        	}
		}
		items.addAll(municipalities);
		
		comboBox = ClientToolkit.getToolkit().createComboBox(items, listener());
	}
	
	@Override
	public IComponent getComponent() {
		return comboBox;
	}

	@Override
	public void setValue(MunicipalityIdentification object) {
		if (object == null) { 
			object = new MunicipalityIdentification();
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

	private static class FederalRegisterMunicipality extends MunicipalityIdentification {
		private static final long serialVersionUID = 1L;
		
		private final FederalRegister federalRegister;

		private FederalRegisterMunicipality(FederalRegister federalRegister) {
			this.federalRegister = federalRegister;
			this.id = - federalRegister.ordinal() - 1;
		}
		
		@Override
		public String toString() {
			return "Bundesregister: " + EnumUtils.getText(federalRegister);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof FederalRegisterMunicipality)) {
				return false;
			}
			FederalRegisterMunicipality other = (FederalRegisterMunicipality) obj;
			return federalRegister == other.federalRegister;
		}
	}

	@Override
	public void mock() {
		setValue(DataGenerator.municipalityIdentification());
	}
}
