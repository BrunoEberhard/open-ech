package ch.openech.client.e10;

import static ch.openech.dm.common.ZipTown.ZIP_TOWN;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.common.ZipTown;

//setTitle("Freie Eingabe Postleitzahl");
//setInformation("<html><b>Hinweis:</b> Normalerweise kann der Ort über die Auswahl ausgewählt werden.<br>Nur bei Ausnahmen sollten hier die Daten direkt eingegeben werden.</html>");
public class ZipTownFreePanel extends EchFormPanel<ZipTown> {
	
	public ZipTownFreePanel() {
		line(ZIP_TOWN.swissZipCode);
		line(ZIP_TOWN.swissZipCodeAddOn);
		line(ZIP_TOWN.swissZipCodeId);
		
		line(ZIP_TOWN.town);
	}
	
//	public Plz getPlz() {
//		Plz plz = new Plz();
//		plz.postleitzahl = IntegerUtils.intValue(get(SWISS_ZIP_CODE));
//		plz.zusatzziffern = IntegerUtils.intValue(get(SWISS_ZIP_CODE_ADD_ON));
//		plz.onrp = IntegerUtils.intValue(get(SWISS_ZIP_CODE_ID));
//		plz.ortsbezeichnung = get(TOWN);
//		return plz;
//	}
//	
//	public void setPlz(Plz plz) {
//		if (plz == null) return;
//		
//		if (plz.postleitzahl > 0) set(SWISS_ZIP_CODE, Integer.toString(plz.postleitzahl));
//		if (plz.zusatzziffern > 0) set(SWISS_ZIP_CODE_ADD_ON, Integer.toString(plz.zusatzziffern));
//		if (plz.onrp > 0) set(SWISS_ZIP_CODE_ID, Integer.toString(plz.onrp));
//
//		if (plz.ortsbezeichnung != null) set(TOWN, plz.ortsbezeichnung.trim());
//	}

}