package ch.openech.frontend.e11;

import static  ch.openech.model.person.Separation.SEPARATION;
import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.person.Separation;
import ch.openech.xml.write.EchSchema;

// Freie Eingabe Trennung
public class SeparationPanel extends EchForm<Separation> {
	
	public SeparationPanel(EchSchema echSchema) {
		super(echSchema);
		
		line(SEPARATION.separation);
		line(SEPARATION.dateOfSeparation);
		if (echSchema.separationTillAvailable()) {
			line(SEPARATION.separationTill);
		}
	}

}
