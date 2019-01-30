package ch.openech.frontend.ech0011;

import java.time.LocalDate;

import org.minimalj.util.DateUtils;

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
		if (s.startsWith("(")) {
			s = s.substring(1).trim();
		}
		if (s.endsWith(")")) {
			s = s.substring(0, s.length() - 1).trim();
		}

		DateRange range = new DateRange();
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

		return range;
	}
}
