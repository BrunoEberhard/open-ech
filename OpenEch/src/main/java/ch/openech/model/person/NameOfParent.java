package  ch.openech.model.person;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;

/**
 * 
 * Verwendet in der Person, einmal für Mutter, einmal für Vater
 */
public class NameOfParent {
	public static final NameOfParent $ = Keys.of(NameOfParent.class);

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
