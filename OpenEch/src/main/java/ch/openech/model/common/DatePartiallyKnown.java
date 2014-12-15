package ch.openech.model.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.model.validation.Validatable;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

public class DatePartiallyKnown implements Validatable, Comparable<DatePartiallyKnown> {

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

	@Override
	public int compareTo(DatePartiallyKnown o) {
		if (value == null) {
			return o.value != null ? -1 : 0;
		} else {
			if (o.value == null) return 1;
			return value.compareTo(o.value);
		}
	}
}
