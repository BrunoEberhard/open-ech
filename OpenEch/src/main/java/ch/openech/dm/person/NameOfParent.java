package ch.openech.dm.person;

import ch.openech.dm.EchFormats;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.util.StringUtils;

/**
 * 
 * Verwendet in der Person, einmal für Mutter, einmal für Vater
 */
public class NameOfParent {
	public static final NameOfParent KEYS = Constants.of(NameOfParent.class);

	@Size(EchFormats.officialFirstName)
	public String firstName;
	@Size(EchFormats.baseName)
	public String officialName;
	
	public Boolean officialProof;
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(firstName) && StringUtils.isEmpty(officialName); 
	}
	
	public String display() {
		if (StringUtils.isEmpty(firstName)) {
			if (!StringUtils.isEmpty(officialName)) {
				return officialName;
			} else {
				return null;
			}
		} else {
			if (!StringUtils.isEmpty(officialName)) {
				return officialName + ", " + firstName;
			} else {
				return firstName;
			}
		}
	}
	
}
