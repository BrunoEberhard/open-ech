package ch.openech.frontend.ech0007;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.Property;
import org.minimalj.util.Codes;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.Mocking;

import ch.ech.ech0071.Canton;
import ch.openech.datagenerator.DataGenerator;

public class CantonFormElement extends AbstractFormElement<Canton> implements Mocking {
	
	private Input<String> textField;

	public CantonFormElement(Object key) {
		this(Keys.getProperty(key));
	}
	
	public CantonFormElement(Property property) {
		super(property);
		textField = Frontend.getInstance().createTextField(2, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", null, listener());
	}
	
	@Override
	public IComponent getComponent() {
		return textField;
	}

	@Override
	public Canton getValue() {
		String text = textField.getValue();
		if (!StringUtils.isEmpty(text)) {
			Canton canton = Codes.get(Canton.class, textField.getValue());
			if (canton == null) {
				canton = new Canton();
				canton.id = textField.getValue();
			}
			return canton;
		} else {
			return null;
		}
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

//	@Override
//	public String validate() {
//		if (!StringUtils.isEmpty(textField.getValue())) {
//			Canton canton = Codes.findCode(Canton.class, textField.getValue());
//			if (canton == null) return "Ungültige Eingabe";
//		}
//		return null;
//	}

}
