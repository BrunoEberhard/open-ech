package ch.openech.frontend.e07;

import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.Validatable;
import org.minimalj.util.Codes;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.Mocking;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.model.common.Canton;

public class CantonFormElement extends AbstractFormElement<Canton> implements Mocking, Validatable {
	
	private TextField textField;

	public CantonFormElement(PropertyInterface property) {
		super(property);
		textField = ClientToolkit.getToolkit().createTextField(2, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", null, null, listener());
	}
	
	@Override
	public IComponent getComponent() {
		return textField;
	}

	@Override
	public Canton getValue() {
		Canton canton = null;
		if (!StringUtils.isEmpty(textField.getValue())) {
			canton = Codes.findCode(Canton.class, textField.getValue());
		}
		return canton;
	}		
	
	@Override
	public void setValue(Canton value) {
		if (value != null) {
			textField.setValue(value.id);
		} else {
			textField.setValue("");
		}
	}

	@Override
	public void mock() {
		setValue(DataGenerator.canton());
	}

	@Override
	public String validate() {
		if (!StringUtils.isEmpty(textField.getValue())) {
			Canton canton = Codes.findCode(Canton.class, textField.getValue());
			if (canton == null) return "Ungültige Eingabe";
		}
		return null;
	}

}
