package ch.openech.frontend.ech0011;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractLookupFormElement.LookupParser;
import org.minimalj.frontend.form.element.FormLookupFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.validation.InvalidValues;

import ch.ech.ech0011.PaperLock;
import ch.ech.ech0046.DateRange;

public class PaperLockFormElement extends FormLookupFormElement<PaperLock> implements LookupParser {

	private static final String LOCKED = "Gesetzt";
	private static final String NOT_LOCKED = "Nicht gesetzt";

	public PaperLockFormElement(PaperLock key, boolean editable) {
		super(Keys.getProperty(key), editable);
	}

	@Override
	protected String render(PaperLock value) {
		if (value != null && value.paperLock != null) {
			StringBuilder s = new StringBuilder();
			if (Boolean.TRUE.equals(value.paperLock)) {
				s.append(LOCKED);
			} else if (Boolean.FALSE.equals(value.paperLock)) {
				s.append(NOT_LOCKED);
			} else if (InvalidValues.isInvalid(value.paperLock)) {
				s.append(InvalidValues.getInvalidValue(value.paperLock));
			}
			RangeUtil.appendRange(s, value.paperLockValidFrom, value.paperLockValidTill);
			return s.toString();
		} else {
			return null;
		}
	}

	@Override
	public PaperLock parse(String text) {
		PaperLock object = new PaperLock();
		if (text != null) {
			text = text.trim();
			int index = text.indexOf('(');
			String lock = text;
			if (index > 0) {
				lock = text.substring(0, index).trim();
				DateRange range = RangeUtil.parseDateRange(text.substring(index));
				object.paperLockValidFrom = range.dateFrom;
				object.paperLockValidTill = range.dateTo;
			}
			if (LOCKED.equals(lock)) {
				object.paperLock = true;
			} else if (NOT_LOCKED.equals(lock)) {
				object.paperLock = false;
			} else {
				object.paperLock = InvalidValues.createInvalidBoolean(lock);
			}
		}
		return object;
	}

	@Override
	public Form<PaperLock> createForm() {
		Form<PaperLock> form = new Form<>(2);
		form.line(PaperLock.$.paperLock);
		form.line(PaperLock.$.paperLockValidFrom, PaperLock.$.paperLockValidTill);
		return form;
	}

}
