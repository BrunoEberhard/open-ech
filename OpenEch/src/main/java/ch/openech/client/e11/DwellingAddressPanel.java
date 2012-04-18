package ch.openech.client.e11;

import static ch.openech.dm.common.DwellingAddress.DWELLING_ADDRESS;

import java.util.List;

import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.xml.write.EchNamespaceContext;

// Wohnadresse
public class DwellingAddressPanel extends EchFormPanel<DwellingAddress> {

	public DwellingAddressPanel(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
		
		line(DWELLING_ADDRESS.EGID);
		line(DWELLING_ADDRESS.EWID);
		if (!namespaceContext.addressesAreBusiness()) {
			line(DWELLING_ADDRESS.householdID);
		}
		area(new AddressField(DWELLING_ADDRESS.mailAddress, true, false, false));
		if (!namespaceContext.addressesAreBusiness()) {
			line(DWELLING_ADDRESS.typeOfHousehold);
		}
		line(DWELLING_ADDRESS.movingDate);
	}
	
	@Override
	public void validate(List<ValidationMessage> resultList) {
		super.validate(resultList);
		// getAddress().validate(resultList);
	}
	
	@Override
	public void fillWithDemoData() {
		super.fillWithDemoData();
		getObject().householdID = "";
	}

}
