package ch.openech.dm.common;

import ch.openech.mj.model.Keys;

public class Canton implements Comparable<Canton> {

	public static final Canton CANTON = Keys.of(Canton.class);
	
    public final CantonAbbreviation cantonAbbreviation = new CantonAbbreviation();
	public String cantonLongName;
	
	@Override
	public int compareTo(Canton c) {
		// Used in ComboBox
		return cantonAbbreviation.canton.compareTo(c.cantonAbbreviation.canton);
	}
}
