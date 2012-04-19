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
		return !StringUtils.isBlank(swissZipCodeId);
	}
	
	public boolean isForeign() {
		return !StringUtils.isBlank(foreignZipCode);
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

	@Override
	public String toString() {
		return display();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((foreignZipCode == null) ? 0 : foreignZipCode.hashCode());
		result = prime * result
				+ ((swissZipCodeId == null) ? 0 : swissZipCodeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Zip other = (Zip) obj;
		if (foreignZipCode == null) {
			if (other.foreignZipCode != null)
				return false;
		} else if (!foreignZipCode.equals(other.foreignZipCode))
			return false;
		if (swissZipCodeId == null) {
			if (other.swissZipCodeId != null)
				return false;
		} else if (!swissZipCodeId.equals(other.swissZipCodeId))
			return false;
		return true;
	}
	
}
