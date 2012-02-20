package ch.openech.dm.common;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.util.StringUtils;

public class CountryZipTown extends ZipTown {

	public static final CountryZipTown COUNTRY_ZIP_TOWN = Constants.of(CountryZipTown.class);
	
	public String country = "CH";
	
	public void appendToHtml(StringBuilder s) {
		if (country != null && !Swiss.SWISS_COUNTRY_ISO2.equals(country)) {
			StringUtils.appendLine(s, country, swissZipCode, foreignZipCode, town); // not displayed: swissZipCodeAddOn, swissZipCodeId
		} else {
			StringUtils.appendLine(s, swissZipCode, foreignZipCode, town); // not displayed: swissZipCodeAddOn, swissZipCodeId
		}
	}	
	
//	@Override
//	protected CountryZipTown clone() {
//		CountryZipTown clone = new CountryZipTown();
//		clone.foreignZipCode = this.foreignZipCode;
//		clone.swissZipCode = this.swissZipCode;
//		clone.swissZipCodeAddOn = this.swissZipCodeAddOn;
//		clone.swissZipCodeId = this.swissZipCodeId;
//		clone.town = this.town;
//		clone.country = this.country;
//		return clone;
//	}
	
}
