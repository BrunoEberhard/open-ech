package  ch.openech.model.person.types;

import org.minimalj.model.annotation.Searched;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.StringLimitation;
import org.minimalj.model.validation.Validatable;
import org.minimalj.util.DemoEnabled;
import org.minimalj.util.StringUtils;

public class Vn implements StringLimitation, Validatable, DemoEnabled {
	private static final int limit = 13;

	@Size(limit) @Searched
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
