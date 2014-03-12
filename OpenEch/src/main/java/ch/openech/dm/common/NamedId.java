package ch.openech.dm.common;

import ch.openech.dm.EchFormats;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.model.annotation.Sizes;

@Sizes(EchFormats.class)
public class NamedId {

	public static final NamedId NAMED_ID = Keys.of(NamedId.class);
	public static final String OPEN_ECH_ID_CATEGORY = "OPENECH.LOC";
	
	public String personIdCategory;
	
	@Size(36)
	public String personId;
	
	public String display() {
		return personIdCategory + "=" + personId;
	}
	
	public void display(StringBuilder s) {
		s.append(personIdCategory); s.append("="); s.append(personId);
	}
	
	public boolean openEch() {
		return OPEN_ECH_ID_CATEGORY.equals(personIdCategory);
	}
	
	public void clear() {
		personIdCategory = null;
		personId = null;
	}
	
}
