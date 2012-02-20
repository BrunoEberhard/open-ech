package ch.openech.client.e11;

import static ch.openech.dm.person.Nationality.NATIONALITY;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.person.Nationality;

// "Freie Eingabe Land"
// 		setInformation("<html><b>Hinweis:</b> Normalerweise kann die Nationalität über die Auswahl ausgewählt werden.<br>Nur bei Ausnahmen sollten hier der Status und die Identifikationsmerkmale des Landes direkt eingegeben werden.</html>");
public class NationalityFreePanel extends EchFormPanel<Nationality> {

	public NationalityFreePanel() {
		line(NATIONALITY.nationalityStatus, NATIONALITY.nationalityCountry.countryId);
		line(NATIONALITY.nationalityCountry.countryNameShort, NATIONALITY.nationalityCountry.countryIdISO2);
	}

}
