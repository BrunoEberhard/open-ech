package ch.openech.client.e101;

import static ch.openech.dm.person.PersonExtendedInformation.PERSON_EXTENDED_INFORMATION;
import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.person.PersonExtendedInformation;

// Ergänzende Personendaten
public class PersonExtendedInformationPanel extends EchFormPanel<PersonExtendedInformation> {

	public PersonExtendedInformationPanel() {
		line(PERSON_EXTENDED_INFORMATION.armedForcesService);
		line(PERSON_EXTENDED_INFORMATION.armedForcesLiability);
		line(PERSON_EXTENDED_INFORMATION.civilDefense);
		line(PERSON_EXTENDED_INFORMATION.fireService);
		line(PERSON_EXTENDED_INFORMATION.fireServiceLiability);
		line(PERSON_EXTENDED_INFORMATION.healthInsured);
		line(PERSON_EXTENDED_INFORMATION.insuranceName);
		area(new AddressField(PERSON_EXTENDED_INFORMATION.insuranceAddress, true, false, true));
		line(PERSON_EXTENDED_INFORMATION.matrimonialInheritanceArrangement);
	}

}
