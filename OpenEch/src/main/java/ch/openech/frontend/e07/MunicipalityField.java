package ch.openech.frontend.e07;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ComboBox;
import org.minimalj.frontend.toolkit.IComponent;
import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.PropertyInterface;
import org.minimalj.util.DemoEnabled;

import ch.openech.datagenerator.DataGenerator;
import  ch.openech.model.code.FederalRegister;
import  ch.openech.model.common.MunicipalityIdentification;
import ch.openech.xml.read.StaxEch0071;

public class MunicipalityField extends AbstractEditField<MunicipalityIdentification> implements DemoEnabled {
	private final List<MunicipalityIdentification> municipalities;
	private final ComboBox<MunicipalityIdentification> comboBox;

	public MunicipalityField(MunicipalityIdentification key, boolean allowFederalRegister) {
		this(Keys.getProperty(key), allowFederalRegister);
	}
	
	public MunicipalityField(PropertyInterface property, boolean allowFederalRegister) {
		super(property, true);
		
		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		
		municipalities = StaxEch0071.getInstance().getMunicipalityIdentifications();
		List<MunicipalityIdentification> items = new ArrayList<MunicipalityIdentification>(municipalities.size() + 5);
		if (allowFederalRegister) {
			for (FederalRegister federalRegister : FederalRegister.values()) {
				items.add(new FederalRegisterMunicipality(federalRegister));
        	}
		}
		items.addAll(municipalities);
		comboBox.setObjects(items);
	}
	
	@Override
	public IComponent getComponent() {
		return comboBox;
	}

	@Override
	public void setObject(MunicipalityIdentification object) {
		if (object == null) { 
			object = new MunicipalityIdentification();
		}
		comboBox.setSelectedObject(object);
	}
	
	@Override
	public MunicipalityIdentification getObject() {
		MunicipalityIdentification municipality = new MunicipalityIdentification();
		if (comboBox.getSelectedObject() != null) {
			comboBox.getSelectedObject().copyTo(municipality);
		}
		return municipality;
	}
	
	// 

	private static class FederalRegisterMunicipality extends MunicipalityIdentification {
		private static final long serialVersionUID = 1L;
		
		private final FederalRegister federalRegister;

		private FederalRegisterMunicipality(FederalRegister federalRegister) {
			this.federalRegister = federalRegister;
			this.historyMunicipalityId = - federalRegister.ordinal() - 1;
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
	public void fillWithDemoData() {
		setObject(DataGenerator.municipalityIdentification());
	}
}
