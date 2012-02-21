package ch.openech.client.e11;

import static ch.openech.dm.person.PlaceOfOrigin.PLACE_OF_ORIGIN;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.person.PlaceOfOrigin;

public class OriginPanel extends EchFormPanel<PlaceOfOrigin> {
	
	public OriginPanel(boolean withReasonAndDate, boolean withExpirationDate) {
		super(2);
		line(PLACE_OF_ORIGIN.originName, PLACE_OF_ORIGIN.canton);
		if (withReasonAndDate) {
			line(PLACE_OF_ORIGIN.reasonOfAcquisition);
			line(PLACE_OF_ORIGIN.naturalizationDate);
//			line(OpenEchCodeField.reasonOfAcquisition());
//			line(new DateField(NATURALIZATION_DATE, DateField.NOT_REQUIRED));
		}
		if (withExpirationDate) {
			line(PLACE_OF_ORIGIN.expatriationDate);
//			line(new DateField(EXPATRIATION_DATE, DateField.NOT_REQUIRED));
		}
	}

}