package ch.openech.frontend;

import java.util.Arrays;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.Keys;
import org.minimalj.util.resources.Resources;

import ch.openech.xml.YesNo;

public class YesNoFormElement extends AbstractFormElement<YesNo> {
	private final Input<String> input;
	private final String yesValue, noValue;

	public YesNoFormElement(Object key, boolean editable) {
		this(key, Resources.getPropertyName(Keys.getProperty(key), "._1"), editable);
	}

	public YesNoFormElement(Object key, String resourceName, boolean editable) {
		super(key);
		noValue = Resources.getString(resourceName + "." + YesNo._0.name());
		yesValue = Resources.getString(resourceName + "." + YesNo._1.name());
		input = Frontend.getInstance().createComboBox(Arrays.asList(noValue, yesValue), listener());
		input.setEditable(editable);
	}

	@Override
	public IComponent getComponent() {
		return input;
	}

	@Override
	public YesNo getValue() {
		String value = input.getValue();
		if (yesValue.equals(value)) {
			return YesNo._1;
		} else if (noValue.equals(value)) {
			return YesNo._0;
		} else {
			return null;
		}
	}

	@Override
	public void setValue(YesNo value) {
		if (value != null) {
			switch (value) {
			case _0:
				input.setValue(noValue);
				break;
			case _1:
				input.setValue(yesValue);
				break;
			}
		} else {
			input.setValue(null);
		}
	}

}
