package ch.openech.frontend;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;

import ch.openech.xml.YesNo;

public class YesNoCheckBoxFormElement extends AbstractFormElement<YesNo> {
	private final Input<Boolean> checkBox;

	public YesNoCheckBoxFormElement(YesNo key, String text, boolean editable) {
		super(key);
		checkBox = Frontend.getInstance().createCheckBox(listener(), text);
		checkBox.setEditable(editable);
	}

	@Override
	public IComponent getComponent() {
		return checkBox;
	}

	@Override
	public YesNo getValue() {
		return Boolean.TRUE.equals(checkBox.getValue()) ? YesNo._1 : YesNo._0;
	}

	@Override
	public void setValue(YesNo value) {
		checkBox.setValue(YesNo._1 == value);
	}

}