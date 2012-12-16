package ch.openech.client.e11;

import static ch.openech.dm.person.Separation.SEPARATION;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.person.Separation;
import ch.openech.xml.write.EchSchema;

// Freie Eingabe Trennung
public class SeparationPanel extends EchFormPanel<Separation> {
	
	public SeparationPanel(EchSchema echSchema) {
		super(echSchema);
		
		line(SEPARATION.separation);
		line(SEPARATION.dateOfSeparation);
		if (echSchema.separationTillAvailable()) {
			line(SEPARATION.separationTill);
		}
	}

}
