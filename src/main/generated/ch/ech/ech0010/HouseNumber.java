package ch.ech.ech0010;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

public class HouseNumber {

	public static final HouseNumber $ = Keys.of(HouseNumber.class);
	
	@Size(30)
	public String houseNumber, dwellingNumber;

	public String concatNumbers() {
		if (StringUtils.isBlank(houseNumber) && StringUtils.isBlank(dwellingNumber)) return null;
	
		if (StringUtils.isBlank(houseNumber)) return dwellingNumber;
		if (StringUtils.isBlank(dwellingNumber)) return houseNumber;
		
		return houseNumber + "&nbsp;/&nbsp;" + dwellingNumber;
	}

}
