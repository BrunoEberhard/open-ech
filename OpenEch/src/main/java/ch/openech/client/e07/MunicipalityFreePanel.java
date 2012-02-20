package ch.openech.client.e07;

import static ch.openech.dm.common.MunicipalityIdentification.MUNICIPALITY_IDENTIFICATION;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.common.MunicipalityIdentification;

//setTitle("Freie Eingabe Ort");
//setInformation("<html><b>Hinweis:</b> Normalerweise kann der Ort über die Auswahl ausgewählt werden.<br>Nur bei Ausnahmen sollten hier die Identifikationsmerkmale des Ortes  direkt eingegeben werden.</html>"); 
public class MunicipalityFreePanel extends EchFormPanel<MunicipalityIdentification> {
	
	public MunicipalityFreePanel() {
		line(MUNICIPALITY_IDENTIFICATION.municipalityId);
		line(MUNICIPALITY_IDENTIFICATION.municipalityName);
		line(MUNICIPALITY_IDENTIFICATION.cantonAbbreviation);
		line(MUNICIPALITY_IDENTIFICATION.historyMunicipalityId);
	}
}
