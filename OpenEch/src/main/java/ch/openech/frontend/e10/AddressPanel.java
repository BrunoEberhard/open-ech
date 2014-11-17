package ch.openech.frontend.e10;

import static  ch.openech.model.common.Address.*;

import org.minimalj.frontend.edit.form.Form;

import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.common.Address;
import ch.openech.util.Plz;
import ch.openech.util.PlzImport;

public class AddressPanel extends EchForm<Address> {
	
	public AddressPanel(boolean swiss, boolean person, boolean organisation) {
		super(4);
		if (organisation) {
			line($.organisationName);
			line($.organisationNameAddOn1);
			line($.organisationNameAddOn2);
		}
		if (person) {
			line($.mrMrs, $.title);
		}
		if (organisation) {
			line($.title);		
		}
		if (organisation || person) {
			line($.firstName, $.lastName);
		}
		line($.addressLine1);
		line($.addressLine2);
		line($.street, $.houseNumber);
		if (!swiss) {
			line($.postOfficeBoxText, $.postOfficeBoxNumber);
			line($.country, $.zip, $.town);
		} else {
			line(new ChIso2Field(), $.zip, $.town);
		}
		line($.locality);
		
		addDependecy($.zip, new TownUpdater(), $.town);
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
