package ch.openech.client.e10;

import static ch.openech.dm.common.Address.ADDRESS;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.common.Address;

public class AddressPanel extends EchFormPanel<Address> {
	
	public AddressPanel(boolean swiss, boolean person, boolean organisation) {
		super(2);
		if (organisation) {
			line(ADDRESS.organisationName);
			line(ADDRESS.organisationNameAddOn1);
			line(ADDRESS.organisationNameAddOn2);
		}
		if (person) {
			line(ADDRESS.mrMrs);
		}
		if (organisation || person) {
			line(ADDRESS.title);
			line(ADDRESS.firstName, ADDRESS.lastName);
		}
		line(ADDRESS.addressLine1);
		line(ADDRESS.addressLine2);
		line(ADDRESS.street, ADDRESS.houseNumber);
		if (!swiss) {
			line(ADDRESS.postOfficeBoxText, ADDRESS.postOfficeBoxNumber);
			line(ADDRESS.countryZipTown);
		} else {
			line(new ZipTownField(ADDRESS.countryZipTown));
		}
		line(ADDRESS.locality);
	}
	
}
