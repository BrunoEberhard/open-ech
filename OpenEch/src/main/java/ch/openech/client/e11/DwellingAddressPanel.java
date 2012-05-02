package ch.openech.client.e11;

import static ch.openech.dm.common.DwellingAddress.DWELLING_ADDRESS;

import java.util.List;

import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.util.StringUtils;
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
		DwellingAddress address = getObject();
		if (!getNamespaceContext().addressesAreBusiness()) {
			if (!StringUtils.isBlank(address.EGID)) {
				if (!StringUtils.isBlank(address.EWID) && !StringUtils.isBlank(address.householdID)) {
					resultList.add(new ValidationMessage(DWELLING_ADDRESS.householdID, "Bei gesetzter EGID können nicht EWID und Haushalt ID gesetzt sein"));
				}
			} else {
				if (StringUtils.isBlank(address.householdID)) {
					resultList.add(new ValidationMessage(DWELLING_ADDRESS.householdID, "Bei fehlender EGID muss die Haushalt ID gesetzt sein"));
				}
			}
		}
		if (address.mailAddress == null || address.mailAddress.isEmpty()) {
			resultList.add(new ValidationMessage(DWELLING_ADDRESS.mailAddress, "Postadresse erforderlich"));
		}
	}
	
	@Override
	public void fillWithDemoData() {
		super.fillWithDemoData();
		getObject().householdID = "";
	}

}
