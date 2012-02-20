package ch.openech.client.e21;

import static ch.openech.dm.person.Occupation.OCCUPATION;
import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.person.Occupation;

// Berufliche Tätigkeit
public class OccupationPanel extends EchFormPanel<Occupation> {
	
	public OccupationPanel() {
		line(OCCUPATION.kindOfEmployment);
		line(OCCUPATION.jobTitle);
		line(OCCUPATION.employer);
		area(new AddressField(OCCUPATION.placeOfWork, false, false, false));
		area(new AddressField(OCCUPATION.placeOfEmployer, false, false, false));
	}

}
