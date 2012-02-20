package ch.openech.client.e10;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.ContextLayout;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.read.StaxEch0072;

public class CountryField extends AbstractEditField<String> implements ChangeListener {

	private final ComboBox comboBox;
	private final TextField textField;
	private final SwitchLayout switchLayout;
	private final ContextLayout contextLayout;
	
	private ZipTownField connectedZipTownField;

	public CountryField() {
		this(null);
	}
		
	public CountryField(Object key) {
		super(key);

		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		comboBox.setObjects(StaxEch0072.getInstance().getCountryIdISO2s());
		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout(); // comboBox, textField
		switchLayout.show(comboBox);
		
		contextLayout = ClientToolkit.getToolkit().createContextLayout(switchLayout);
		
		// editable 
		
		createMenu();
	}
	
	@Override
	public Object getComponent() {
		return contextLayout;
	}

	private void createMenu() {
		List<Action> actions = new ArrayList<Action>();
		Action select = new AbstractAction("Auswahl Land") {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchLayout.show(comboBox);
				comboBox.requestFocus();
			}
        };
        actions.add(select);

        Action freeEntry = new AbstractAction("Freie Eingabe") {
			@Override
			public void actionPerformed(ActionEvent e) {				
				switchLayout.show(textField);
				textField.setText(null);
				comboBox.requestFocus();
			}
        };
        actions.add(freeEntry);
        
        contextLayout.setActions(actions);
	}
	
	public void setConnectedZipTownField(ZipTownField connectedZipTownField) {
		this.connectedZipTownField = connectedZipTownField;
	}
	
	@Override
    public void stateChanged(ChangeEvent e) {
		updateZipTowField();
		fireChange();
	}
    
	private void updateZipTowField() {
		if (connectedZipTownField != null) {
			String country = getObject();
			boolean foreign = !"CH".equals(country) && !"LI".equals(country);
			connectedZipTownField.setForeign(foreign);
		}
	}
	
	@Override
	public String getObject() {
		if (switchLayout.getShownComponent() == textField) {
			if (StringUtils.isBlank(textField.getText())) {
				return null;
			} else {
				return textField.getText();
			}
		} else {
			return (String) comboBox.getSelectedObject();
		} 
	}

	@Override
	public void setObject(String value) {
		if (StringUtils.isBlank(value)) {
			switchLayout.show(comboBox);
			comboBox.setSelectedObject("CH");
		} else {
			switchLayout.show(comboBox);
			comboBox.setSelectedObject(value);
		}
	}
	
	@Override
	public void setValidationMessages(List<ValidationMessage> validationMessages) {
		textField.setValidationMessages(validationMessages);
		comboBox.setValidationMessages(validationMessages);
	}
	
}
