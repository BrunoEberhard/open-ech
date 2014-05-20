package ch.openech.frontend.e11;

import static  ch.openech.model.person.PlaceOfOrigin.PLACE_OF_ORIGIN;
import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.person.PlaceOfOrigin;

public class OriginPanel extends EchForm<PlaceOfOrigin> {
	
	public OriginPanel(boolean withReasonAndDate, boolean withExpirationDate) {
		super(2);
		line(PLACE_OF_ORIGIN.originName, PLACE_OF_ORIGIN.cantonAbbreviation.canton);
		if (withReasonAndDate) {
			line(PLACE_OF_ORIGIN.reasonOfAcquisition);
			line(PLACE_OF_ORIGIN.naturalizationDate);
		}
		if (withExpirationDate) {
			line(PLACE_OF_ORIGIN.expatriationDate);
		}
	}

}
