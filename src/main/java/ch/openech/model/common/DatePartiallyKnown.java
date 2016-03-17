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
	
	@Override
	public String toString() {
		return DateUtils.format(value);
	}
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(value);
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
	
	public void mock() {
		int year = (int) (Math.random() * 80) + 1930;
		if (Math.random() > 0.98) {
			value = "" + year;
			return;
		}
		int month = (int) (Math.random() * 12) + 1;
		if (Math.random() > 0.98) {
			value = year + "-" + month;
			return;
		}
		int day;
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			day = (int) (Math.random() * 30) + 1;
		} else if (month == 2) {
			day = (int) (Math.random() * 28) + 1;
		} else {
			day = (int) (Math.random() * 31) + 1;
		}
		value = year + "-" + month + "-" + day;
	}

}
