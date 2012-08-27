package ch.openech.dm.common;

import static ch.openech.mj.db.model.annotation.PredefinedFormat.String36;

import java.util.UUID;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Is;

public class NamedId {

	public static final NamedId NAMED_ID = Constants.of(NamedId.class);
	public static final String OPEN_ECH_ID_CATEGORY = "OPENECH.LOC";
	
	public String personIdCategory;
	
	@Is(String36)
	public String personId;
	
	public String display() {
		return personIdCategory + "=" + personId;
	}
	
	public void display(StringBuilder s) {
		s.append(personIdCategory); s.append("="); s.append(personId);
	}
	
	public boolean isOpenEch() {
		return OPEN_ECH_ID_CATEGORY.equals(personIdCategory);
	}
	
	public void setOpenEch() {
		personIdCategory = OPEN_ECH_ID_CATEGORY;
		personId = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
	}
	
}
