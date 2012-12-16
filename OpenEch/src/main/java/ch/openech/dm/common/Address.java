package ch.openech.dm.common;

import ch.openech.dm.EchFormats;
import ch.openech.dm.types.MrMrs;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.EnumUtils;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.model.annotation.Sizes;
import ch.openech.mj.util.StringUtils;

@Sizes(EchFormats.class)
public class Address {
	
	public static final Address ADDRESS = Constants.of(Address.class);

	// organisation
	@Size(EchFormats.organisationName)
	public String organisationName, organisationNameAddOn1, organisationNameAddOn2;

	// person
	public MrMrs mrMrs;
	
	// organisation oder person
	public String title;
	public String firstName, lastName;
	
	// all
	@Size(EchFormats.addressLine)
	public String addressLine1, addressLine2;
	public String street;
	public final HouseNumber houseNumber = new HouseNumber();
	@Size(4)
	public Integer postOfficeBoxNumber;
	public String postOfficeBoxText;
	public String locality;
	public String country = "CH";
	public final Zip zip = new Zip();
	@Required
	public String town;
	
	public boolean isEmpty() {
		return StringUtils.isBlank(town);
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

		StringUtils.appendLine(s, EnumUtils.getText(mrMrs), title, firstName, lastName);
		StringUtils.appendLine(s, addressLine1);
		StringUtils.appendLine(s, addressLine2);
		StringUtils.appendLine(s, street, houseNumber.concatNumbers());
		StringUtils.appendLine(s, postOfficeBoxText, postOfficeBoxNumber != null ? postOfficeBoxNumber.toString() : null);
		if ("CH".equals(country)) {
			StringUtils.appendLine(s, zip.display(), town);
		} else {
			StringUtils.appendLine(s, country, zip.display(), town);
		}
		StringUtils.appendLine(s, locality);
	}

}
