package ch.openech.dm.common;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.util.StringUtils;

public class Zip {
	public static final Zip ZIP_TOWN = Constants.of(Zip.class);
	
	public String foreignZipCode;
	public String swissZipCode; // 0 - 9999
	public String swissZipCodeAddOn;
	public String swissZipCodeId; // int

	public void setPlz(Plz plz) {
		foreignZipCode = null;
		swissZipCode = Integer.toString(plz.postleitzahl);
		swissZipCodeAddOn = Integer.toString(plz.zusatzziffern);
		swissZipCodeId = Integer.toString(plz.onrp);
	}

	public boolean isEmpty() {
		return StringUtils.isBlank(swissZipCode) && StringUtils.isBlank(foreignZipCode);
	}
	
	public boolean isSwiss() {
		return StringUtils.isBlank(foreignZipCode);
	}

	public void clear() {
		foreignZipCode = null;
		swissZipCode = swissZipCodeAddOn = swissZipCodeId = null;
	}
	
	public String display() {
		if (!StringUtils.isBlank(swissZipCode)) {
			if (!StringUtils.isBlank(swissZipCodeAddOn) && !"0".equals(swissZipCodeAddOn)) {
				return swissZipCode + " " + swissZipCodeAddOn;
			} else {
				return swissZipCode;
			}
		 } else if (!StringUtils.isBlank(foreignZipCode)) {
			 return foreignZipCode;
		 } else {
			 return "";
		 }
	}
	
}
