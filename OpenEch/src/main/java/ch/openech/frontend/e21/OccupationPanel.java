package ch.openech.frontend.e21;

import static  ch.openech.model.person.Occupation.*;
import ch.openech.frontend.e10.AddressField;
import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.person.Occupation;
import ch.openech.xml.write.EchSchema;

// Berufliche Tätigkeit
public class OccupationPanel extends EchForm<Occupation> {
	
	public OccupationPanel(EchSchema echSchema) {
		super(echSchema, 2);
		
		line(OCCUPATION.kindOfEmployment);
		line(OCCUPATION.jobTitle);
		line(OCCUPATION.employer);
		line(new AddressField(OCCUPATION.placeOfWork, false, false, false), new AddressField(OCCUPATION.placeOfEmployer, false, false, false));
		if (echSchema.occupationValidTillAvailable()) {
			line(OCCUPATION.occupationValidTill);
		}
	}

}
