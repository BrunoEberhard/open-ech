package  ch.openech.model.common;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;

public class TechnicalIds {
	public static final TechnicalIds $ = Keys.of(TechnicalIds.class);
	
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
