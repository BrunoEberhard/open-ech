package ch.openech.client.e10;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.openech.dm.common.Address;
import ch.openech.dm.common.CountryIdentification;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.TextField;
import ch.openech.xml.read.StaxEch0072;

public class ChIso2Field extends AbstractEditField<String> implements ChangeListener {
	private final TextField textField;
	
	public ChIso2Field() {
		super(Address.ADDRESS.country, true);

		textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		textField.setText("CH");
	}
	
		@Override
	public Object getComponent() {
		return textField;
	}

	
	@Override
    public void stateChanged(ChangeEvent e) {
		fireChange();
	}
	
	@Override
	public String getObject() {
		return "CH";
	}

	@Override
	public void setObject(String object) {
		// not supported
	}
	
}
