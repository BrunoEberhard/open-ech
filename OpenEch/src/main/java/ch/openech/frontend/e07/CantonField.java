package ch.openech.frontend.e07;

import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.IComponent;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.InvalidValues;
import org.minimalj.model.PropertyInterface;
import org.minimalj.util.DemoEnabled;

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
