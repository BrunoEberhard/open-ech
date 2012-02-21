package ch.openech.dm.common;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.util.StringUtils;

public class HouseNumber implements Cloneable {

	public static final HouseNumber HOUSE_NUMBER = Constants.of(HouseNumber.class);
	
	public String houseNumber;
	public String dwellingNumber;

	public String concatNumbers() {
		if (StringUtils.isBlank(houseNumber) && StringUtils.isBlank(dwellingNumber)) return null;
	
		if (StringUtils.isBlank(houseNumber)) return dwellingNumber;
		if (StringUtils.isBlank(dwellingNumber)) return houseNumber;
		
		return houseNumber + "&nbsp;/&nbsp;" + dwellingNumber;
	}

	@Override
	protected HouseNumber clone() {
		HouseNumber clone = new HouseNumber();
		clone.houseNumber = this.houseNumber;
		clone.dwellingNumber = this.dwellingNumber;
		return clone;
	}
	
}