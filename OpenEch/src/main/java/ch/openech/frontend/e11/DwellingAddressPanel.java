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
		
		line(DWELLING_ADDRESS.EGID);
		line(DWELLING_ADDRESS.EWID);
		if (!echSchema.addressesAreBusiness()) {
			line(DWELLING_ADDRESS.householdID);
		}
		line(new AddressField(DWELLING_ADDRESS.mailAddress, true, false, false));
		if (!echSchema.addressesAreBusiness()) {
			line(DWELLING_ADDRESS.typeOfHousehold);
		}
		line(DWELLING_ADDRESS.movingDate);
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
