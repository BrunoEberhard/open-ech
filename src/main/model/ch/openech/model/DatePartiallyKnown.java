package ch.openech.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

public class DatePartiallyKnown implements Validation, Comparable<DatePartiallyKnown> {
	public static final DatePartiallyKnown $ = Keys.of(DatePartiallyKnown.class);
	
	@Size(10)
	public String value;
	
	@Override
	public List<ValidationMessage> validate() {
		if (InvalidValues.isInvalid(value)) {
			return Validation.message($.value, "Kein gültiges Datum");
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
	
	
	public LocalDate getYearMonthDay() {
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

	public void setYearMonthDay(LocalDate yearMonthDay) {
		if (yearMonthDay != null) {
			this.value = yearMonthDay.toString();
		} else {
			this.value = null;
		}
	}

	public String getYearMonth() {
		if (value != null && value.length() == 7) {
			return value;
		} else {
			return null;
		}
	}

	public void setYearMonth(String yearMonth) {
		if (yearMonth != null) {
			if (yearMonth.length() == 7) {
				this.value = yearMonth;
			} else {
				throw new IllegalArgumentException(yearMonth);
			}
		} else {
			this.value = null;
		}
	}

	public Integer getYear() {
		if (value != null && value.length() == 4) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException x) {
				return null;
			}
		} else {
			return null;
		}
	}

	public void setYear(Integer year) {
		if (year != null) {
			if (year >= 1000 && year < 10000) {
				this.value = Integer.toString(year);
			} else {
				throw new IllegalArgumentException("" + year);
			}
		} else {
			this.value = null;
		}
	}

}