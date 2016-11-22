package  ch.openech.model.organisation;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.StringUtils;
import org.minimalj.util.mock.Mocking;

public class UidStructure implements Validation, Mocking {

	public static final UidStructure $ = Keys.of(UidStructure.class);
	private static final int[] mult = {5, 4, 3, 2, 7, 6, 5, 4};

	public static final int LENGTH = 12;
	
	// ADM000000001 - CHE999999999
	@Size(LENGTH)
	public String value;

	
	@Override
	public List<ValidationMessage> validate() {
		if (value == null || value.length() < LENGTH) {
			return Validation.message($.value, "Es sind 3 Buchstaben und 9 Ziffern erforderlich");
		}
		String organisationIdCategory = value.substring(0, 3);
		if (!StringUtils.equals(organisationIdCategory, "ADM", "CHE")) {
			return Validation.message($.value, "Die ersten drei Buchstaben müssen ADM oder CHE lauten");
		}
		for (int i = 3; i<value.length(); i++) {
			if (!Character.isDigit(value.charAt(i))) {
				return Validation.message($.value, "Die Eingabe muss ausser den ersten drei Buchstaben aus Ziffern bestehen");
			}
		}
		if (!checksum(value)) {
			return Validation.message($.value, "Checksumme ungültig");
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
	public void mock() {
		do {
			value = Math.random() < 5 ? "ADM" : "CHE";
			value += (int)(Math.random() * 900000000 + 100000000);
		} while (!checksum(value));
	}
	
}
