package ch.openech.frontend.e07;

import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.PropertyInterface;
import org.minimalj.model.validation.Validatable;
import org.minimalj.util.Codes;
import org.minimalj.util.DemoEnabled;
import org.minimalj.util.StringUtils;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.model.common.Canton;

public class CantonField extends AbstractEditField<Canton> implements DemoEnabled, Validatable {
	
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
	public Canton getObject() {
		Canton canton = null;
		if (!StringUtils.isEmpty(textField.getText())) {
			canton = Codes.findCode(Canton.class, textField.getText());
		}
		return canton;
	}		
	
	@Override
	public void setObject(Canton value) {
		if (value != null) {
			textField.setText(value.id);
		} else {
			textField.setText("");
		}
	}

	@Override
	public void fillWithDemoData() {
		setObject(DataGenerator.canton());
	}

	@Override
	public String validate() {
		if (!StringUtils.isEmpty(textField.getText())) {
			Canton canton = Codes.findCode(Canton.class, textField.getText());
			if (canton == null) return "Ungültige Eingabe";
		}
		return null;
	}

}
