package ch.openech.dm.person;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.Swiss;
import ch.openech.mj.db.model.Constants;

public class Nationality  {

	public static final Nationality NATIONALITY = Constants.of(Nationality.class);
	
	public String nationalityStatus = "2";
	public final CountryIdentification nationalityCountry = Swiss.createCountryIdentification();
	
	public boolean isSwiss() {
		return "2".equals(nationalityStatus) && nationalityCountry.isSwiss();
	}

	public void copyTo(Nationality copy) {
		copy.nationalityStatus = nationalityStatus;
		nationalityCountry.copyTo(copy.nationalityCountry);
	}
	
//	public boolean isEqual(Nationality nationality) {
//		return StringUtils.equals(nationality.nationalityStatus, this.nationalityStatus) && //
//			sameValues(nationality.nationalityCountry, this.nationalityCountry);
//	}

//	@Override
//	public Nationality clone() {
//		Nationality clone = new Nationality();
//		clone.nationalityStatus = this.nationalityStatus;
//		clone.nationalityCountry = this.nationalityCountry.clone();
//		return clone;
//	}
}
