package ch.openech.client.e08;

import static ch.openech.dm.common.CountryIdentification.COUNTRY_IDENTIFICATION;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.common.CountryIdentification;

//setTitle("Freie Eingabe Land");
//setInformation("<html><b>Hinweis:</b> Normalerweise kann das Land über die Auswahl ausgewählt werden.<br>Nur bei Ausnahmen sollten hier die Identifikationsmerkmale des Landes direkt eingegeben werden.</html>");
public class CountryFreePanel extends EchFormPanel<CountryIdentification> {
	
	public CountryFreePanel() {
		line(COUNTRY_IDENTIFICATION.countryId);
		line(COUNTRY_IDENTIFICATION.countryNameShort);
		line(COUNTRY_IDENTIFICATION.countryIdISO2);
	}

}
