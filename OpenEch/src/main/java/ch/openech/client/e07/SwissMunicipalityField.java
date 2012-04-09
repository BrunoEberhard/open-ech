package ch.openech.client.e07;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ch.openech.dm.code.EchCodes;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.db.model.Code;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.read.StaxEch0071;

public class SwissMunicipalityField extends ObjectField<MunicipalityIdentification> {
	private static final Logger logger = Logger.getLogger(SwissMunicipalityField.class.getName());

	private final boolean allowFederalRegister;
	private final List<MunicipalityIdentification> municipalities;
	
	private final ComboBox comboBox;

	public SwissMunicipalityField(boolean allowFederalRegister) {
		this(null, allowFederalRegister);
	}
	
	public SwissMunicipalityField(Object key, boolean allowFederalRegister) {
		super(Constants.getConstant(key));
		this.allowFederalRegister = allowFederalRegister;
		
		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		
		municipalities = StaxEch0071.getInstance().getMunicipalityIdentifications();
		List<String> municipalityNames = new ArrayList<String>(municipalities.size() + 2);
		if (allowFederalRegister) {
			final Code code = EchCodes.federalRegister;
			for (int i = 0; i<3; i++) {
				municipalityNames.add("Bundesregister: " + code.getText(i));
        	}
		}
		for (MunicipalityIdentification municipality : municipalities) {
			municipalityNames.add(municipality.municipalityName);
		}
		comboBox.setObjects(municipalityNames);
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
		
		String value = (String) comboBox.getSelectedObject();
		if (allowFederalRegister) {
			final Code code = EchCodes.federalRegister;
			for (int i = 0; i<3; i++) {
				if (value.equals("Bundesregister: " + code.getText(i))) {
					municipality.municipalityId = "-" + code.getKey(i);
				}
        	}
		}
		
		for (MunicipalityIdentification m : municipalities) {
			if (StringUtils.equals(m.municipalityName, value)) {
				m.copyTo(municipality);
				break;
			}
		}
		
		return municipality;
	}
	
	// 

	@Override
	protected void show(MunicipalityIdentification object) {
		if (allowFederalRegister) {
			if (object.isFederalRegister()) {
				final Code code = EchCodes.federalRegister;
				int key = Integer.parseInt(object.getFederalRegister());
				comboBox.setSelectedObject("Bundesregister: " + code.getText(key));
			} 
		}

		int index = StaxEch0071.getInstance().getMunicipalityIdentifications().indexOf(object);
		if (index >= 0) {
			comboBox.setSelectedObject(object.municipalityName);
		} else {
			logger.warning("Unknown Municipality");
			comboBox.setSelectedObject(null);
		}
	}

}
