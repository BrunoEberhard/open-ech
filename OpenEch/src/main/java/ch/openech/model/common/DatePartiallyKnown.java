package ch.openech.model.common;

import org.minimalj.model.InvalidValues;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validatable;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;

public class DatePartiallyKnown implements Validatable {

	@Size(10)
	public String value;
	
	@Override
	public String validate() {
		if (InvalidValues.isInvalid(value)) {
			return "Kein gültiges Datum";
		} else {
			return null;
		}
	}

	public LocalDate toLocalDate() {
		if (!StringUtils.isEmpty(value)) {
			try {
				return LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
			} catch (DateTimeParseException x) {
				// silent
				return null;
			}
		} else {
			return null;
		}
	}
	
	public String toString() {
		return DateUtils.format(value);
	}
}
