package ch.openech.frontend.e11;

import static  ch.openech.model.common.DwellingAddress.*;
import ch.openech.frontend.e10.AddressField;
import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.common.DwellingAddress;
import ch.openech.xml.write.EchSchema;

// Wohnadresse
public class DwellingAddressPanel extends EchForm<DwellingAddress> {
	
	public DwellingAddressPanel(EchSchema echSchema) {
		super(echSchema);
		
		line($.EGID);
		line($.EWID);
		if (!echSchema.addressesAreBusiness()) {
			line($.householdID);
		}
		line(new AddressField($.mailAddress, true, false, false));
		if (!echSchema.addressesAreBusiness()) {
			line($.typeOfHousehold);
		}
		line($.movingDate);
	}
	
	public void setObject(DwellingAddress address) {
		address.echSchema = echSchema;
		super.setObject(address);
	}
	
	@Override
	protected void fillWithDemoData(DwellingAddress dwellingAddress) {
		super.fillWithDemoData(dwellingAddress);
		dwellingAddress.householdID = "";
	}
}
