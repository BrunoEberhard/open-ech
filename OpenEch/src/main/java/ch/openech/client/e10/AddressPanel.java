package ch.openech.client.e10;

import static ch.openech.dm.common.Address.ADDRESS;
import ch.openech.client.ewk.event.EchForm;
import ch.openech.dm.common.Address;

public class AddressPanel extends EchForm<Address> {
	
	public AddressPanel(boolean swiss, boolean person, boolean organisation) {
		super(4);
		if (organisation) {
			line(ADDRESS.organisationName);
			line(ADDRESS.organisationNameAddOn1);
			line(ADDRESS.organisationNameAddOn2);
		}
		if (person) {
			line(ADDRESS.mrMrs, ADDRESS.title);
		}
		if (organisation) {
			line(ADDRESS.title);		
		}
		if (organisation || person) {
			line(ADDRESS.firstName, ADDRESS.lastName);
		}
		line(ADDRESS.addressLine1);
		line(ADDRESS.addressLine2);
		line(ADDRESS.street, ADDRESS.houseNumber);
		if (!swiss) {
			line(ADDRESS.postOfficeBoxText, ADDRESS.postOfficeBoxNumber);
			line(ADDRESS.country, ADDRESS.zip, ADDRESS.town);
		} else {
			line(new ChIso2Field(), ADDRESS.zip, ADDRESS.town);
		}
		line(ADDRESS.locality);
	}

	@Override
	protected int getColumnWidthPercentage() {
		return 70;
	}
	
}
