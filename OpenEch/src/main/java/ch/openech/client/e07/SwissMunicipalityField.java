package ch.openech.client.e07;

import java.util.ArrayList;
import java.util.List;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.dm.code.EchCodes;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.xml.read.StaxEch0071;

public class SwissMunicipalityField extends AbstractEditField<MunicipalityIdentification> implements DemoEnabled {
	private final List<MunicipalityIdentification> municipalities;
	private final ComboBox<MunicipalityIdentification> comboBox;

	public SwissMunicipalityField(boolean allowFederalRegister) {
		this(null, allowFederalRegister);
	}
	
	public SwissMunicipalityField(Object key, boolean allowFederalRegister) {
		super(Constants.getConstant(key), true);
		
		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		
		municipalities = StaxEch0071.getInstance().getMunicipalityIdentifications();
		List<MunicipalityIdentification> items = new ArrayList<MunicipalityIdentification>(municipalities.size() + 5);
		if (allowFederalRegister) {
			for (int i = 0; i<3; i++) {
				items.add(new FederalRegisterMunicipality(i));
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

	@Override
	public boolean isEmpty() {
		return getObject().isEmpty();
	}

	private static class FederalRegisterMunicipality extends MunicipalityIdentification {
		private final int key;

		private FederalRegisterMunicipality(int key) {
			this.key = key;
			this.municipalityId = "-" + key;
		}
		
		@Override
		public String toString() {
			return "Bundesregister: " + EchCodes.federalRegister.getText(key);
		}

		@Override
		public int hashCode() {
			return key;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof FederalRegisterMunicipality)) {
				return false;
			}
			FederalRegisterMunicipality other = (FederalRegisterMunicipality) obj;
			return key == other.key;
		}
	}

	@Override
	public void fillWithDemoData() {
		setObject(DataGenerator.municipalityIdentification());
	}
}
