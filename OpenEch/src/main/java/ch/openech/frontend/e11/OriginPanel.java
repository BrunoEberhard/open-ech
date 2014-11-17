package ch.openech.frontend.e11;

import static ch.openech.model.person.PlaceOfOrigin.*;
import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.model.person.PlaceOfOrigin;

public class OriginPanel extends EchForm<PlaceOfOrigin> {
	
	public OriginPanel(boolean withReasonAndDate, boolean withExpirationDate) {
		super(2);
		line($.originName, $.canton);
		if (withReasonAndDate) {
			line($.reasonOfAcquisition);
			line($.naturalizationDate);
		}
		if (withExpirationDate) {
			line($.expatriationDate);
		}
	}

}
