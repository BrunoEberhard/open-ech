package ch.openech.dm.person.types;

import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.model.annotation.LimitedString;
import ch.openech.mj.util.StringUtils;

public class Vn implements LimitedString, Validatable, DemoEnabled {
	private static final int limit = 13;

	@Size(limit)
	public String value;
	
	@Override
	public int getMaxLength() {
		return limit;
	}

	@Override
	public String getAllowedCharacters() {
		return "0123456789";
	}

	@Override
	public String validate() {
		if (StringUtils.isEmpty(value))
			return null;

		long vn = 0;
		try {
			vn = Long.parseLong(value);
		} catch (NumberFormatException e) {
			// silent
		}
		if (vn < 7560000000001L || vn > 7569999999999L) {
			return "Die Eingabe muss zw 7560000000001 und 7569999999999 liegen";
		}
		return null;
	}

	@Override
	public void fillWithDemoData() {
		Long n = 1 + (long)(9999999999L * Math.random());
		value = "756" + StringUtils.padLeft(n.toString(), 10, '0');
	}

}
