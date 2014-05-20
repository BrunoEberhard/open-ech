package ch.openech.frontend.e101;

import static  ch.openech.model.person.PersonExtendedInformation.*;
import ch.openech.frontend.e10.AddressField;
import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.person.PersonExtendedInformation;

// Ergänzende Personendaten
public class PersonExtendedInformationPanel extends EchForm<PersonExtendedInformation> {

	public PersonExtendedInformationPanel() {
		line(PERSON_EXTENDED_INFORMATION.armedForcesService);
		line(PERSON_EXTENDED_INFORMATION.armedForcesLiability);
		line(PERSON_EXTENDED_INFORMATION.civilDefense);
		line(PERSON_EXTENDED_INFORMATION.fireService);
		line(PERSON_EXTENDED_INFORMATION.fireServiceLiability);
		line(PERSON_EXTENDED_INFORMATION.healthInsured);
		line(PERSON_EXTENDED_INFORMATION.insuranceName);
		line(new AddressField(PERSON_EXTENDED_INFORMATION.insuranceAddress, true, false, true));
		line(PERSON_EXTENDED_INFORMATION.matrimonialInheritanceArrangement);
	}

}
