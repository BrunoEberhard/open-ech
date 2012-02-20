package ch.openech.dm.contact;

import ch.openech.mj.db.model.annotation.FormatName;

// Neu mit CodeWithOther gelöst
@Deprecated
public class ContactEntryCategory {

	public String category;
	
	@FormatName("freeKategoryText")
	public String otherCategory;

}
