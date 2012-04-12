package ch.openech.dm.common;

import ch.openech.mj.db.model.Constants;

public class Canton implements Comparable<Canton> {

	public static final Canton CANTON = Constants.of(Canton.class);
	
    public String cantonAbbreviation;
	public String cantonLongName;
	
	@Override
	public int compareTo(Canton c) {
		// Used in ComboBox
		return cantonAbbreviation.compareTo(c.cantonAbbreviation);
	}
}
