package ch.openech.dm.common;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.util.StringUtils;

public class ZipTown {

	public static final ZipTown ZIP_TOWN = Constants.of(ZipTown.class);
	
	public String foreignZipCode;
	public String swissZipCode; // 0 - 9999
	public String swissZipCodeAddOn;
	public String swissZipCodeId; // int
	public String town;
	
	@Override
	protected ZipTown clone() {
		ZipTown clone = new ZipTown();
		clone.foreignZipCode = this.foreignZipCode;
		clone.swissZipCode = this.swissZipCode;
		clone.swissZipCodeAddOn = this.swissZipCodeAddOn;
		clone.swissZipCodeId = this.swissZipCodeId;
		clone.town = this.town;
		return clone;
	}

	public void setPlz(Plz plz) {
		foreignZipCode = null;
		swissZipCode = Integer.toString(plz.postleitzahl);
		swissZipCodeAddOn = Integer.toString(plz.zusatzziffern);
		swissZipCodeId = Integer.toString(plz.onrp);
		town = plz.ortsbezeichnung;
	}

	public boolean isEmpty() {
		return StringUtils.isBlank(town);
	}
	
	public boolean isSwiss() {
		return StringUtils.isBlank(foreignZipCode);
	}

	public void clear() {
		foreignZipCode = null;
		swissZipCode = swissZipCodeAddOn = swissZipCodeId = null;
		town = null;
	}
	
}
