package ch.openech.client.e07;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import ch.openech.dm.code.EchCodes;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.db.model.Code;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.ObjectField;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.resources.ResourceAction;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.read.StaxEch0071;

public class SwissMunicipalityField extends ObjectField<MunicipalityIdentification> {
	private final boolean allowFederalRegister;
	
	private final ComboBox comboBoxMunicipality;
	private final TextField textMunicipality;
	private final TextField textFederalRegister;
	private final SwitchLayout switchLayout;

	public SwissMunicipalityField(boolean allowFederalRegister) {
		this(null, allowFederalRegister);
	}
	
	public SwissMunicipalityField(Object key, boolean allowFederalRegister) {
		super(Constants.getConstant(key));
		this.allowFederalRegister = allowFederalRegister;
		
		comboBoxMunicipality = ClientToolkit.getToolkit().createComboBox(listener());
		comboBoxMunicipality.setObjects(StaxEch0071.getInstance().getMunicipalityIdentifications());
		textMunicipality = ClientToolkit.getToolkit().createReadOnlyTextField();
		textFederalRegister = ClientToolkit.getToolkit().createReadOnlyTextField();
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayout.show(comboBoxMunicipality);
		
        modeSelectMunicipality();
		
        createMenu();
	}
	
	@Override
	public Object getComponent() {
		return decorateWithContextActions(switchLayout);
	}

	private void createMenu() {
		addContextAction(new MunicipalitySelectAction());
		addContextAction(new ObjectFieldEditor());
		addContextAction(new MunicipalityRemoveAction());
        
        if (allowFederalRegister) {
        	final Code code = EchCodes.federalRegister;
        	for (int i = 0; i<3; i++) {
        		final int key = Integer.parseInt(code.getKey(i));
        		Action federalRegisterAction = new AbstractAction("Bundesregister: " + code.getText(i)) {
        			@Override
        			public void actionPerformed(ActionEvent e) {
        				MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
        				municipalityIdentification.municipalityId = "-" + key;
        				setObject(municipalityIdentification);
        			}
                };
                addContextAction(federalRegisterAction);
        	}
        }
	}
	
	private void modeSelectMunicipality() {
		switchLayout.show(comboBoxMunicipality);
		comboBoxMunicipality.requestFocus();
	}
	
	private void modeFreeMunicipality() {
		textMunicipality.setText(getObject() != null ? getObject().municipalityName : null);
		switchLayout.show(textMunicipality);
	}
	
	private void modeUnknown() {
		comboBoxMunicipality.setSelectedObject(null);
		modeSelectMunicipality();
	}

	private void modeFederalRegister() {
		int federalRegister = getObject().historyMunicipalityId.charAt(1) - '0';

		comboBoxMunicipality.setSelectedObject(null);
		textFederalRegister.setText(EchCodes.federalRegister.getText("" + federalRegister)); 
		switchLayout.show(textFederalRegister);
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
		if (switchLayout.getShownComponent() == comboBoxMunicipality) {
			return (MunicipalityIdentification) comboBoxMunicipality.getSelectedObject();
		} else if (switchLayout.getShownComponent() == textFederalRegister) {
			return super.getObject();
		} else {
			if (!StringUtils.isBlank(textMunicipality.getText())) {
				MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
				municipalityIdentification.municipalityName = textMunicipality.getText();
				return municipalityIdentification;
			} else {
				return null;
			}
		}
	}
	
	// 
	
	private final class MunicipalityRemoveAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {		
			modeUnknown();
		}
	}

	private final class MunicipalitySelectAction extends ResourceAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			modeSelectMunicipality();
			
		}
	}

	@Override
	protected void show(MunicipalityIdentification object) {
		if (object.isFederalRegister()) {
			modeFederalRegister();
			return;
		} 

		int index = StaxEch0071.getInstance().getMunicipalityIdentifications().indexOf(object);
		if (index >= 0) {
			comboBoxMunicipality.setSelectedObject(object);
			modeSelectMunicipality();
		} else {
			comboBoxMunicipality.setSelectedObject(null);
			modeFreeMunicipality();
			textMunicipality.setText(object.municipalityName);
		}
	}

	@Override
	public FormVisual<MunicipalityIdentification> createFormPanel() {
		return new MunicipalityFreePanel();
	}
	
}
