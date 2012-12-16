package ch.openech.dm.organisation;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.util.StringUtils;

public class UidStructure implements Validatable {

	public static final UidStructure UIDSTRUCTURE_STRUCTURE = Constants.of(UidStructure.class);
	public static final int LENGTH = 12;
	
	// ADM000000001 - CHE999999999
	@Size(LENGTH)
	public String value;

	@Override
	public String validate() {
		if (StringUtils.isEmpty(value)) return null;
		
		if (value.length() != LENGTH) {
			return "Länge muss " + LENGTH + " sein";
		} else {
			if (!(value.startsWith("ADM")|| value.startsWith("CHE"))) {
				return "Muss mit ADM oder CHE beginnen";
			}
		}
		return null;
	}
	
	
}
