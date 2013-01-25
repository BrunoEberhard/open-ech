package ch.openech.client.e07;

import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.model.InvalidValues;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;

public class CantonField extends AbstractEditField<String> implements DemoEnabled {
	
	private TextField textField;

	public CantonField(PropertyInterface property) {
		this(property, true);
	}
	
	public CantonField(PropertyInterface property, boolean editable) {
		super(property, editable);
		
		if (editable) {
			textField = ClientToolkit.getToolkit().createTextField(listener(), 2, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"); // new IndicatingTextField(new LimitedDocument());
		} else {
			textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		}
	}
	
	@Override
	public IComponent getComponent() {
		return textField;
	}

	@Override
	public String getObject() {
		return textField.getText();
	}		
	
	@Override
	public void setObject(String value) {
		if (InvalidValues.isInvalid(value)) {
			textField.setText(InvalidValues.getInvalidValue(value));
		} else {
			textField.setText(value);
		}
	}

	@Override
	public void fillWithDemoData() {
		setObject("SO");
	}

}
