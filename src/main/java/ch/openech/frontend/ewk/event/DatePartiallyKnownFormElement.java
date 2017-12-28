package ch.openech.frontend.ewk.event;

import org.minimalj.frontend.form.element.FormatFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import ch.openech.model.common.DatePartiallyKnown;

public class DatePartiallyKnownFormElement extends FormatFormElement<DatePartiallyKnown> {

	public DatePartiallyKnownFormElement(DatePartiallyKnown key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}
	
	public DatePartiallyKnownFormElement(PropertyInterface property, boolean editable) {
		super(property, editable);
	}

	@Override
	public void mock() {
		DatePartiallyKnown datePartiallyKnown = new DatePartiallyKnown();
		datePartiallyKnown.mock();
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
	public DatePartiallyKnown parse(String string) {
		DatePartiallyKnown datePartiallyKnown = new DatePartiallyKnown();
		datePartiallyKnown.value = DateUtils.parseCH(string, true);
		if (StringUtils.isEmpty(datePartiallyKnown.value)) {
			datePartiallyKnown.value = InvalidValues.createInvalidString(string);
		}
		return datePartiallyKnown;
	}

	@Override
	public String render(DatePartiallyKnown datePartiallyKnown) {
		return datePartiallyKnown != null ? datePartiallyKnown.toString() : "";
	}
	
}
