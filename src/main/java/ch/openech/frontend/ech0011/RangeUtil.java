package ch.openech.frontend.ech0011;

import java.time.LocalDate;

import org.minimalj.model.validation.InvalidValues;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import ch.ech.ech0046.DateRange;

public class RangeUtil {

	public static void appendRange(StringBuilder stringBuilder, LocalDate from, LocalDate to) {
		appendRange(stringBuilder, from, to, true);
	}

	public static void appendRangeLine(StringBuilder stringBuilder, LocalDate from, LocalDate to) {
		appendRange(stringBuilder, from, to, false);
	}

	public static void appendRange(StringBuilder stringBuilder, LocalDate from, LocalDate to, boolean inline) {
		if (from != null || to != null) {
			if (inline) {
				if (stringBuilder.length() > 0 && !Character.isWhitespace(stringBuilder.charAt(stringBuilder.length() - 1))) {
					stringBuilder.append(' ');
				}
				stringBuilder.append('(');
			}
			if (from != null && to != null) {
				stringBuilder.append(DateUtils.format(from)).append(" - ").append(DateUtils.format(to));
			} else if (from != null) {
				stringBuilder.append("Ab ").append(DateUtils.format(from));
			} else {
				stringBuilder.append("Bis ").append(DateUtils.format(to));
			}
			if (inline) {
				stringBuilder.append(')');
			}
		}
	}

	public static DateRange parseDateRange(String s) {
		DateRange range = new DateRange();
		if (StringUtils.isBlank(s)) {
			return range;
		}

		if (s.startsWith("(")) {
			s = s.substring(1).trim();
		}
		if (s.endsWith(")")) {
			s = s.substring(0, s.length() - 1).trim();
		}

		int index = s.indexOf('-');
		if (index > -1) {
			range.dateFrom = DateUtils.parse(s.substring(0, index).trim());
			range.dateTo = index < s.length() - 1 ? DateUtils.parse(s.substring(index + 1).trim()) : null;
			return range;
		}

		s = s.toLowerCase();
		index = s.indexOf("ab");
		if (index > -1) {
			range.dateFrom = DateUtils.parse(s.substring(index + 2).trim());
			return range;
		}

		index = s.indexOf("bis");
		if (index > -1) {
			range.dateTo = DateUtils.parse(s.substring(index + 3).trim());
			return range;
		}

		if (range.dateFrom == null && range.dateTo == null) {
			range.dateFrom = InvalidValues.createInvalidLocalDate("Bereich ungültig");
		}

		return range;
	}

	public static void appendValidFrom(StringBuilder stringBuilder, LocalDate from) {
		appendValidFrom(stringBuilder, from, true);
	}

	public static void appendValidFrom(StringBuilder stringBuilder, LocalDate from, boolean inline) {
		if (from != null) {
			if (inline) {
				if (stringBuilder.length() > 0
						&& !Character.isWhitespace(stringBuilder.charAt(stringBuilder.length() - 1))) {
					stringBuilder.append(' ');
				}
				stringBuilder.append('(');
			}
			stringBuilder.append("Ab ").append(DateUtils.format(from));
			if (inline) {
				stringBuilder.append(')');
			}
		}
	}

	public static LocalDate parseValidFrom(String s) {
		if (StringUtils.isBlank(s)) {
			return null;
		}

		if (s.startsWith("(")) {
			s = s.substring(1).trim();
		}
		if (s.endsWith(")")) {
			s = s.substring(0, s.length() - 1).trim();
		}

		s = s.toLowerCase();
		int index = s.indexOf("ab");
		if (index > -1) {
			return DateUtils.parse(s.substring(index + 2).trim());
		}

		return InvalidValues.createInvalidLocalDate("Bereich ungültig");
	}

}
