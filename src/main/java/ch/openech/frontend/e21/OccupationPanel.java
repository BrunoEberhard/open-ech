package ch.openech.frontend.e21;

import static  ch.openech.model.person.Occupation.*;
import ch.openech.frontend.e10.AddressFormElement;
import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.person.Occupation;
import ch.openech.xml.write.EchSchema;

// Berufliche Tätigkeit
public class OccupationPanel extends EchForm<Occupation> {
	
	public OccupationPanel(EchSchema echSchema) {
		super(echSchema, 2);
		
		line($.kindOfEmployment);
		line($.jobTitle);
		line($.employer);
		line(new AddressFormElement($.placeOfWork, false, false, false), new AddressFormElement($.placeOfEmployer, false, false, false));
		if (echSchema.occupationValidTillAvailable()) {
			line($.occupationValidTill);
		}
	}

}
