package ch.openech.dm.common;

import ch.openech.mj.db.model.Constants;

public class Canton implements Cloneable, Comparable<Canton> {

	public static final Canton CANTON = Constants.of(Canton.class);
	
    public String cantonAbbreviation;
	public String cantonLongName;
	
	@Override
	protected Canton clone() {
		Canton clone = new Canton();
		clone.cantonAbbreviation = this.cantonAbbreviation;
		clone.cantonLongName = this.cantonLongName;
		return clone;
	}

	@Override
	public int compareTo(Canton c) {
		// Used in ComboBox
		return cantonAbbreviation.compareTo(c.cantonAbbreviation);
	}
}
