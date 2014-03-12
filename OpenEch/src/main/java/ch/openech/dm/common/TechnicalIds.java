package ch.openech.dm.common;

import java.util.ArrayList;
import java.util.List;

import ch.openech.mj.model.Keys;

public class TechnicalIds {
	public static final TechnicalIds TECHNICAL_IDS = Keys.of(TechnicalIds.class);
	
	public final NamedId localId = new NamedId();
	public final List<NamedId> otherId = new ArrayList<NamedId>();
	public final List<NamedId> euId = new ArrayList<NamedId>();

	public TechnicalIds() {
	}
	
	public void clear() {
		localId.clear();
		otherId.clear();
		euId.clear();
	}

}
