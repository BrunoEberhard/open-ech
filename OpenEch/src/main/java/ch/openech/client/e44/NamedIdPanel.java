package ch.openech.client.e44;

import ch.openech.client.ewk.event.EchForm;
import ch.openech.dm.common.NamedId;

public class NamedIdPanel extends EchForm<NamedId> {
	
	public NamedIdPanel() {
		line(NamedId.NAMED_ID.personIdCategory);
		line(NamedId.NAMED_ID.personId);
	}

}
