package ch.openech.frontend.ewk.event;

import org.minimalj.frontend.form.element.FormatFormElement;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.MockDate;

import ch.openech.model.common.DatePartiallyKnown;

public class DatePartiallyKnownFormElement extends FormatFormElement<DatePartiallyKnown> {

	public DatePartiallyKnownFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	@Override
	public void mock() {
		DatePartiallyKnown datePartiallyKnown = new DatePartiallyKnown();
		datePartiallyKnown.value = MockDate.generateRandomDatePartiallyKnown();
		setValue(datePartiallyKnown);
	}

	@Override
	protected String getAllowedCharacters(PropertyInterface property) {
		return "0123456789.";
	}

	@Override
	protected int getAllowedSize(PropertyInterface property) {
		return 10;
	}

	@Override
	public DatePartiallyKnown getValue() {
		DatePartiallyKnown datePartiallyKnown = new DatePartiallyKnown();
		datePartiallyKnown.value = DateUtils.parseCH(textField.getValue(), true);
		if (StringUtils.isEmpty(datePartiallyKnown.value)) {
			datePartiallyKnown.value = InvalidValues.createInvalidString(textField.getValue());
		}
		return datePartiallyKnown;
	}

	@Override
	public void setValue(DatePartiallyKnown datePartiallyKnown) {
		textField.setValue(datePartiallyKnown != null ? datePartiallyKnown.toString() : "");
	}
	
}