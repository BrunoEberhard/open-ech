package ch.openech.dm.common;

import ch.openech.dm.EchFormats;
import ch.openech.dm.code.EchCodes;
import ch.openech.dm.code.MrMrs;
import ch.openech.mj.db.model.ColumnAccess;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.util.StringUtils;

public class Address {
	
	public static final Address ADDRESS = Constants.of(Address.class);

	// organisation
	@FormatName(EchFormats.organisationName)
	public String organisationName, organisationNameAddOn1, organisationNameAddOn2;

	// person
	public String mrMrs;
	
	// organisation oder person
	public String title;
	public String firstName, lastName;
	
	// all
	@FormatName(EchFormats.addressLine)
	public String addressLine1, addressLine2;
	public String street;
	public final HouseNumber houseNumber = new HouseNumber();
	public String postOfficeBoxNumber;
	public String postOfficeBoxText;
	public String locality;
	public final CountryZipTown countryZipTown = new CountryZipTown();
	
	@Override
	public Address clone() {
		Address address = new Address();
		ColumnAccess.copy(this, address);
		return address;
	}
	
	public void setMrMrs(MrMrs mrMrs) {
		this.mrMrs = mrMrs.getKey();
	}
	
	public boolean isEmpty() {
		return StringUtils.isBlank(street) || StringUtils.isBlank(countryZipTown.town);
 	}
	
	public boolean isOrganisation() {
		// Bei Organisation muss der OrganisationName gesetzt sein, alle anderen können wegfallen
		return !StringUtils.isBlank(organisationName);
	}
	
	public boolean isPerson() {
		// Bei Person muss lastName gesetzt sein
		return !StringUtils.isBlank(lastName);
	}
	
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		s.append("<HTML>");
		toHtml(s);
		s.append("</HTML>");
		return s.toString();
	}
	
	public void toHtml(StringBuilder s) {
		StringUtils.appendLine(s, organisationName);
		StringUtils.appendLine(s, organisationNameAddOn1);
		StringUtils.appendLine(s, organisationNameAddOn2);

		StringUtils.appendLine(s, EchCodes.mrMrs.getText(mrMrs), title, firstName, lastName);
		StringUtils.appendLine(s, addressLine1);
		StringUtils.appendLine(s, addressLine2);
		StringUtils.appendLine(s, street, houseNumber.concatNumbers());
		StringUtils.appendLine(s, postOfficeBoxText, postOfficeBoxNumber);
		countryZipTown.appendToHtml(s);
		StringUtils.appendLine(s, locality);
	}

}