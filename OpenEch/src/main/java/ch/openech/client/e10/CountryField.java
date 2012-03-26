package ch.openech.client.e10;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.read.StaxEch0072;

public class CountryField extends AbstractEditField<String> implements ChangeListener {

	private final ComboBox comboBox;
	private final TextField textField;
	private final SwitchLayout switchLayout;
	
	private ZipTownField connectedZipTownField;

	public CountryField() {
		this(null);
	}
		
	public CountryField(Object key) {
		super(key, true);

		comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		comboBox.setObjects(StaxEch0072.getInstance().getCountryIdISO2s());
		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		
		switchLayout = ClientToolkit.getToolkit().createSwitchLayout(); // comboBox, textField
		switchLayout.show(comboBox);
		
		// editable 
		
		createMenu();
	}
	
		@Override
	public Object getComponent() {
		return decorateWithContextActions(switchLayout);
	}

	private void createMenu() {
		Action select = new AbstractAction("Auswahl Land") {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchLayout.show(comboBox);
				comboBox.requestFocus();
			}
        };
        addContextAction(select);

        Action freeEntry = new AbstractAction("Freie Eingabe") {
			@Override
			public void actionPerformed(ActionEvent e) {				
				switchLayout.show(textField);
				textField.setText(null);
				comboBox.requestFocus();
			}
        };
        addContextAction(freeEntry);
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
	
}
