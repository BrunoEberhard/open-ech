package ch.openech.dm.common;

import ch.openech.dm.EchFormats;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.model.annotation.Sizes;
import ch.openech.mj.util.StringUtils;
import ch.openech.util.Plz;

@Sizes(EchFormats.class)
public class Zip {
	public static final Zip ZIP_TOWN = Constants.of(Zip.class);
	
	public String foreignZipCode;
	@Size(4)
	public Integer swissZipCode; // 0 - 9999
	public Integer swissZipCodeAddOn;
	@Size(4)
	public Integer swissZipCodeId; // int

	public void setPlz(Plz plz) {
		foreignZipCode = null;
		swissZipCode = plz.postleitzahl;
		swissZipCodeAddOn = plz.zusatzziffern;
		swissZipCodeId = plz.onrp;
	}

	public boolean isEmpty() {
		return swissZipCode == null && foreignZipCode == null;
	}
	
	public boolean isSwiss() {
		return swissZipCodeId != null;
	}
	
	public boolean isForeign() {
		return !StringUtils.isBlank(foreignZipCode);
	}

	public void clear() {
		foreignZipCode = null;
		swissZipCode = swissZipCodeAddOn = swissZipCodeId = null;
	}
	
	public String display() {
		if (swissZipCode != null) {
			if (swissZipCodeAddOn != null && swissZipCodeAddOn != 0) {
				return swissZipCode + " " + swissZipCodeAddOn;
			} else {
				return String.valueOf(swissZipCode);
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
