package ch.openech.frontend.ewk.event;

import org.minimalj.autofill.DateGenerator;
import org.minimalj.frontend.edit.fields.TextFormatField;
import org.minimalj.model.InvalidValues;
import org.minimalj.model.PropertyInterface;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import ch.openech.model.common.DatePartiallyKnown;

public class DatePartiallyKnownField extends TextFormatField<DatePartiallyKnown> {

	public DatePartiallyKnownField(PropertyInterface property) {
		super(property);
	}
	
	public DatePartiallyKnownField(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	@Override
	public void fillWithDemoData() {
		DatePartiallyKnown datePartiallyKnown = new DatePartiallyKnown();
		datePartiallyKnown.value = DateGenerator.generateRandomDatePartiallyKnown();
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
