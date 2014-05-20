package  ch.openech.model.common;

import org.minimalj.model.Keys;

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
