package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.validation.InvalidValues;

import ch.ech.ech0011.DataLock;
import ch.ech.ech0046.DateRange;

public class DataLockFormElement extends FormLookupFormElement<DataLock> implements LookupParser {

	public DataLockFormElement(DataLock key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected String render(DataLock value) {
		if (value != null && value.dataLock != null) {
			StringBuilder s = new StringBuilder();
			s.append(EnumUtils.getText(value.dataLock));
			RangeUtil.appendRange(s, value.dataLockValidFrom, value.dataLockValidTill);
			return s.toString();
		} else {
			return null;
		}
	}

	@Override
	public DataLock parse(String text) {
		DataLock object = new DataLock();
		if (text != null) {
			text = text.trim();
			int index = text.indexOf('(');
			String separationString = text;
			if (index > 0) {
				separationString = text.substring(0, index).trim();
				DateRange range = RangeUtil.parseDateRange(text.substring(index));
				object.dataLockValidFrom = range.dateFrom;
				object.dataLockValidTill = range.dateTo;
			}
			for (ch.ech.ech0021.DataLock s : ch.ech.ech0021.DataLock.values()) {
				if (EnumUtils.getText(s).equals(separationString)) {
					object.dataLock = s;
					break;
				}
			}
			if (object.dataLock == null) {
				object.dataLock = InvalidValues.createInvalidEnum(ch.ech.ech0021.DataLock.class, separationString);
			}
		}
		return object;
	}

	@Override
	public Form<DataLock> createForm() {
		Form<DataLock> form = new Form<>(2);
		form.line(DataLock.$.dataLock);
		form.line(DataLock.$.dataLockValidFrom, DataLock.$.dataLockValidTill);
		return form;
	}

}
