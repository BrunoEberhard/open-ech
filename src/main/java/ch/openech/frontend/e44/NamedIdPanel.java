package ch.openech.frontend.e44;

import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.common.NamedId;

public class NamedIdPanel extends EchForm<NamedId> {
	
	public NamedIdPanel() {
		line(NamedId.$.IdCategory);
		line(NamedId.$.Id);
	}

}
