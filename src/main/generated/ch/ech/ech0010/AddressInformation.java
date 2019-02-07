package ch.ech.ech0010;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;

import ch.openech.util.PlzImport;

public class AddressInformation implements Validation {
	public static final AddressInformation $ = Keys.of(AddressInformation.class);

	@Size(150)
	public String addressLine1, addressLine2, street;
	@Size(30)
	public String houseNumber, dwellingNumber;
	@Size(8)
	public Integer postOfficeBoxNumber;
	@Size(15)
	public String postOfficeBoxText;
	@Size(40)
	public String locality;
	@NotEmpty
	@Size(40)
	public String town;
	@Size(4)
	public Integer swissZipCode;
	@Size(2)
	public String swissZipCodeAddOn;
	public Integer swissZipCodeId;
	@Size(15)
	public String foreignZipCode;
	@NotEmpty
	public ch.ech.ech0008.Country country;

	public void render(StringBuilder s) {
		StringUtils.appendLine(s, addressLine1);
		StringUtils.appendLine(s, addressLine2);
		StringUtils.appendLine(s, street, houseNumber, dwellingNumber);
		StringUtils.appendLine(s, postOfficeBoxText, postOfficeBoxNumber != null ? postOfficeBoxNumber.toString() : null);
		if (country != null) {
			if (StringUtils.equals(country.iso2Id, "CH")) {
				StringUtils.appendLine(s, "" + swissZipCode, town);
			} else {
				StringUtils.appendLine(s, country.iso2Id, "" + swissZipCode, town);
			}
		}
		StringUtils.appendLine(s, locality);
	}

	public boolean isSwissOrLichtenstein() {
		return country == null || StringUtils.equals(country.iso2Id, "CH", "LI");
	}

	@Override
	public List<ValidationMessage> validate() {
		List<ValidationMessage> messages = new ArrayList<>();
		if (isSwissOrLichtenstein()) {
			boolean zipEmpty = swissZipCode == null || swissZipCode == 0;
			boolean zipInvalid = !zipEmpty && (swissZipCode < 1000 || swissZipCode >= 10000);
			if (zipEmpty) {
				messages.add(new ValidationMessage($.swissZipCode, Resources.getString("EmptyValidator.messageNoCaption")));
			} else if (zipInvalid) {
				messages.add(new ValidationMessage($.swissZipCode, Resources.getString("ObjectValidator.message")));
			}

			boolean townEmpty = StringUtils.isEmpty(town);
			if (!zipEmpty && !townEmpty) {
				int zip = swissZipCode;
				long count = PlzImport.getInstance().getPlzList().stream()
						.filter(plz -> (plz.postleitzahl == zip && StringUtils.equals(town, plz.ortsbezeichnung))).count();
				if (count != 1) {
					messages.add(new ValidationMessage($.swissZipCode, Resources.getString("AddressInformation.invalidZipTown")));
				}
			}
		}
		return messages;
	}

}