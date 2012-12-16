package ch.openech.client.e21;

import static ch.openech.dm.person.Occupation.OCCUPATION;
import ch.openech.client.e10.AddressField;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.person.Occupation;
import ch.openech.xml.write.EchSchema;

// Berufliche Tätigkeit
public class OccupationPanel extends EchFormPanel<Occupation> {
	
	public OccupationPanel(EchSchema echSchema) {
		super(2);
		
		line(OCCUPATION.kindOfEmployment);
		line(OCCUPATION.jobTitle);
		line(OCCUPATION.employer);
		area(new AddressField(OCCUPATION.placeOfWork, false, false, false), new AddressField(OCCUPATION.placeOfEmployer, false, false, false));
		if (echSchema.occupationValidTillAvailable()) {
			line(OCCUPATION.occupationValidTill);
		}
	}

}
