package  ch.openech.model.organisation;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.StringLimitation;
import org.minimalj.model.validation.Validatable;
import org.minimalj.util.DemoEnabled;
import org.minimalj.util.StringUtils;

public class UidStructure implements StringLimitation, Validatable, DemoEnabled {

	public static final UidStructure UIDSTRUCTURE_STRUCTURE = Keys.of(UidStructure.class);
	private static final int[] mult = {5, 4, 3, 2, 7, 6, 5, 4};

	public static final int LENGTH = 12;
	
	// ADM000000001 - CHE999999999
	@Size(LENGTH)
	public String value;

	
	@Override
	public String validate() {
		if (value == null || value.length() < LENGTH) {
			return "Es sind 3 Buchstaben und 9 Ziffern erforderlich";
		}
		String organisationIdCategory = value.substring(0, 3);
		if (!StringUtils.equals(organisationIdCategory, "ADM", "CHE")) {
			return "Die ersten drei Buchstaben müssen ADM oder CHE lauten";
		}
		for (int i = 3; i<value.length(); i++) {
			if (!Character.isDigit(value.charAt(i))) {
				return "Die Eingabe muss ausser den ersten drei Buchstaben aus Ziffern bestehen";
			}
		}
		if (!checksum(value)) {
			return "Checksumme ungültig";
		}
		return null;
	};
	
	public static boolean checksum(String value) {
		int sum = 0;
		for (int i = 3; i<value.length()-1; i++) {
			sum += (value.charAt(i) - '0') * mult[i - 3];
		}
		sum = sum % 11;
		return sum == (value.charAt(value.length()-1) - '0');
	}
	
	@Override
	public void fillWithDemoData() {
		do {
			value = Math.random() < 5 ? "ADM" : "CHE";
			value += (int)(Math.random() * 900000000 + 100000000);
		} while (!checksum(value));
	}

	@Override
	public int getMaxLength() {
		return LENGTH;
	}

	@Override
	public String getAllowedCharacters() {
		return "ACDHEM0123456789";
	}
	
}
