package ch.openech.client.e10;

import static ch.openech.dm.common.Address.*;
import ch.openech.client.ewk.event.EchForm;
import ch.openech.dm.common.Address;
import ch.openech.mj.edit.form.Form;
import ch.openech.util.Plz;
import ch.openech.util.PlzImport;

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
		
		addDependecy(ADDRESS.zip, new TownUpdater(), ADDRESS.town);
	}

	@Override
	protected int getColumnWidthPercentage() {
		return 70;
	}

	public static class TownUpdater implements Form.PropertyUpdater<String, String, Address> {

		@Override
		public String update(String input, Address address) {
			if (address.isSwiss()) {
				// Adresse enthält noch den alten Wert. Updater werden aufgerufen, bevor
				// der neue Wert in das Objekt geschrieben wurde. Die folgenden getter
				// würden ohne diese Zuweisung somit die alten Werte lieferen.
				address.zip = input;
				
				String swissZipCode = address.getSwissZipCode();
				String swissZipCodeAddOn = address.getSwissZipCodeAddOn();
				Plz plz = PlzImport.getInstance().getPlz(swissZipCode, swissZipCodeAddOn);
				if (plz != null) {
					return plz.ortsbezeichnung;
				}
			}
			// Keine Änderung, einfach gleichen Wert zurückgeben
			return address.town;
		}
		
	}

}
