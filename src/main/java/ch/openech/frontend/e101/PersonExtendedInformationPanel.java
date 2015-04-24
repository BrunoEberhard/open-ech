package ch.openech.frontend.e101;

import static  ch.openech.model.person.PersonExtendedInformation.*;
import ch.openech.frontend.e10.AddressFormElement;
import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.person.PersonExtendedInformation;

// Ergänzende Personendaten
public class PersonExtendedInformationPanel extends EchForm<PersonExtendedInformation> {

	public PersonExtendedInformationPanel() {
		line($.armedForcesService);
		line($.armedForcesLiability);
		line($.civilDefense);
		line($.fireService);
		line($.fireServiceLiability);
		line($.healthInsured);
		line($.insuranceName);
		line(new AddressFormElement($.insuranceAddress, true, false, true));
		line($.matrimonialInheritanceArrangement);
	}

}
