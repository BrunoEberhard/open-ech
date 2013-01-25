package ch.openech.client.e07;

import java.util.ArrayList;
import java.util.List;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.code.FederalRegister;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.EnumUtils;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
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
