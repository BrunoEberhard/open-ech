package  ch.openech.model.person.types;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Searched;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validatable;
import org.minimalj.util.DemoEnabled;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;

public class Vn implements Validatable, DemoEnabled {

	@Size(EchFormats.vn) @Searched
	public String value;
	
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
			return "Wert muss zw 7560000000001 und 7569999999999 liegen " + vn;
		}
		if (!isValidEAN13(value)) {
			return "Checksumme falsch";
		}
		return null;
	}
	
	public String getFormattedValue() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "formattedValue", String.class);
		
		if (value != null && value.length() == 13) {
			 return value.substring(0,3) + "." + value.substring(3,7) + "." + value.substring(7,11) + "." + value.substring(11,13);
		} else {
			return value;
		}
	}

	@Override
	public void fillWithDemoData() {
		Long n = 1 + (long)(999999999L * Math.random());
		value = "756" + StringUtils.padLeft(n.toString(), 9, '0');
		value += calculate(value);
	}

	// see apache's EAN13CheckDigit
	
	private boolean isValidEAN13(String code) {
		try {
			int modulusResult = calculateModulus(code, true);
			return (modulusResult == 0);
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}
	
	private int calculate(String code) {
		int modulusResult = calculateModulus(code, false);
		int charValue = (10 - modulusResult) % 10;
		return charValue;
	}

	private int calculateModulus(String code, boolean includesCheckDigit) {
		int total = 0;
		for (int i = 0; i < code.length(); i++) {
			int lth = code.length() + (includesCheckDigit ? 0 : 1);
			int leftPos = i + 1;
			int rightPos = lth - i;
			int charValue = toInt(code.charAt(i), leftPos, rightPos);
			total += weightedValue(charValue, leftPos, rightPos);
		}
		if (total == 0) {
			throw new IllegalArgumentException("Invalid code, sum is zero");
		}
		return (total % 10);
	}

	private static final int[] POSITION_WEIGHT = new int[] { 3, 1 };

	private int weightedValue(int charValue, int leftPos, int rightPos) {
		int weight = POSITION_WEIGHT[rightPos % 2];
		return (charValue * weight);
	}

	private int toInt(char character, int leftPos, int rightPos) {
		if (Character.isDigit(character)) {
			return Character.getNumericValue(character);
		} else {
			throw new IllegalArgumentException("Invalid Character[" + leftPos + "] = '" + character + "'");
		}
	}
}
