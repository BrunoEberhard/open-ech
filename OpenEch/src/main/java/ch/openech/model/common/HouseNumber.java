package  ch.openech.model.common;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Sizes;
import org.minimalj.util.StringUtils;

import  ch.openech.model.EchFormats;

@Sizes(EchFormats.class)
public class HouseNumber {

	public static final HouseNumber $ = Keys.of(HouseNumber.class);
	
	public String houseNumber;
	public String dwellingNumber;

	public String concatNumbers() {
		if (StringUtils.isBlank(houseNumber) && StringUtils.isBlank(dwellingNumber)) return null;
	
		if (StringUtils.isBlank(houseNumber)) return dwellingNumber;
		if (StringUtils.isBlank(dwellingNumber)) return houseNumber;
		
		return houseNumber + "&nbsp;/&nbsp;" + dwellingNumber;
	}

}
