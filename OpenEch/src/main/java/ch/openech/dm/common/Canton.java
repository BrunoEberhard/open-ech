package ch.openech.dm.common;

import ch.openech.mj.model.Constants;

public class Canton implements Comparable<Canton> {

	public static final Canton CANTON = Constants.of(Canton.class);
	
    public final CantonAbbreviation cantonAbbreviation = new CantonAbbreviation();
	public String cantonLongName;
	
	@Override
	public int compareTo(Canton c) {
		// Used in ComboBox
		return cantonAbbreviation.canton.compareTo(c.cantonAbbreviation.canton);
	}
}
