package ch.openech.client.e07;

import java.util.ArrayList;
import java.util.List;

import ch.openech.dm.code.EchCodes;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.xml.read.StaxEch0071;

public class SwissMunicipalityField extends ObjectField<MunicipalityIdentification> {
	private final List<MunicipalityIdentification> municipalities;
	private final ComboBox<MunicipalityIdentification> comboBox;

	public SwissMunicipalityField(boolean allowFederalRegister) {
		this(null, allowFederalRegister);
	}
	
	public SwissMunicipalityField(Object key, boolean allowFederalRegister) {
		super(Constants.getConstant(key));
		
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
	public Object getComponent() {
		return comboBox;
	}

	@Override
	public void setObject(MunicipalityIdentification object) {
		if (object == null) { 
			object = new MunicipalityIdentification();
		}
		super.setObject(object);
	}
	
	@Override
	public MunicipalityIdentification getObject() {
		MunicipalityIdentification municipality = super.getObject();
		
		if (comboBox.getSelectedObject() != null) {
			comboBox.getSelectedObject().copyTo(municipality);
		} else {
			municipality.clear();
		}
		return municipality;
	}
	
	// 

	@Override
	protected void show(MunicipalityIdentification object) {
		comboBox.setSelectedObject(object);
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
}
