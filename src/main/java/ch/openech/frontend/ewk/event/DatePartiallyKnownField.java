package ch.openech.frontend.ewk.event;

import org.minimalj.frontend.edit.fields.TextFormatField;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.MockDate;

import ch.openech.model.common.DatePartiallyKnown;

public class DatePartiallyKnownField extends TextFormatField<DatePartiallyKnown> {

	public DatePartiallyKnownField(PropertyInterface property) {
		super(property);
	}
	
	public DatePartiallyKnownField(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	@Override
	public void mock() {
		DatePartiallyKnown datePartiallyKnown = new DatePartiallyKnown();
		datePartiallyKnown.value = MockDate.generateRandomDatePartiallyKnown();
		setObject(datePartiallyKnown);
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
	public DatePartiallyKnown getObject() {
		DatePartiallyKnown datePartiallyKnown = new DatePartiallyKnown();
		datePartiallyKnown.value = DateUtils.parseCH(textField.getText(), true);
		if (StringUtils.isEmpty(datePartiallyKnown.value)) {
			datePartiallyKnown.value = InvalidValues.createInvalidString(textField.getText());
		}
		return datePartiallyKnown;
	}

	@Override
	public void setObject(DatePartiallyKnown datePartiallyKnown) {
		textField.setText(datePartiallyKnown != null ? datePartiallyKnown.toString() : "");
	}
	
}
