package ch.openech.dm.common;

import java.util.ArrayList;
import java.util.List;

public class TechnicalIds {

	public final NamedId localId = new NamedId();
	public final List<NamedId> otherId = new ArrayList<NamedId>();
	public final List<NamedId> euId = new ArrayList<NamedId>();

	public void clear() {
		otherId.clear();
		euId.clear();
	}

}
