package ch.openech.client.e11;

import static ch.openech.dm.person.Separation.SEPARATION;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.person.Separation;

// Freie Eingabe Trennung
public class SeparationPanel extends EchFormPanel<Separation> {
	
	public SeparationPanel() {
		line(SEPARATION.separation);
		line(SEPARATION.dateOfSeparation);
		line(SEPARATION.separationTill);
//		line(new DateField(DATE_OF_SEPARATION, DateField.NOT_REQUIRED));
//		line(new DateField(SEPARATION_TILL, DateField.NOT_REQUIRED));
	}

}