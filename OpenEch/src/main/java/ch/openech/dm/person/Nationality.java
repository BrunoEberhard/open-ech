package ch.openech.dm.person;

import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.Swiss;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.util.StringUtils;

public class Nationality  {

	public static final Nationality NATIONALITY = Constants.of(Nationality.class);
	
	public String nationalityStatus = "2";
	public final CountryIdentification nationalityCountry = Swiss.createCountryIdentification();
	
	public Nationality() {
		// do nothing
	}
	
	private Nationality(String nationalityStatus) {
		this.nationalityStatus = nationalityStatus;
	}
	
	public static final Nationality newUnknown() {
		return new Nationality("0");
	}

	public static final Nationality newWithout() {
		return new Nationality("1");
	}

	public boolean isSwiss() {
		return "2".equals(nationalityStatus) && nationalityCountry.isSwiss();
	}

	public void copyTo(Nationality copy) {
		copy.nationalityStatus = nationalityStatus;
		nationalityCountry.copyTo(copy.nationalityCountry);
	}

	@Override
	public int hashCode() {
		if ("2".equals(nationalityStatus)) {
			return nationalityCountry.hashCode();
		} else if (nationalityStatus == null) {
			return 0;
		} else {
			return nationalityStatus.hashCode();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Nationality)) {
			return false;
		}
		Nationality other = (Nationality) obj;
		if (!StringUtils.equals(nationalityStatus, other.nationalityStatus)) {
			return false;
		}
		if ("2".equals(nationalityStatus)) {
			return other.nationalityCountry.equals(nationalityCountry);
		} else {
			return true;
		}
	}

	@Override
	public String toString() {
		if ("0".equals(nationalityStatus)) {
			return "Staatenlos";
		} else if ("1".equals(nationalityStatus)) {
			return "Staatsangehörigkeit unbekannt";
		} else if ("2".equals(nationalityStatus)) {
			return nationalityCountry.toString();
		} else {
			return nationalityStatus + " / " + nationalityCountry.toString();
		}
	}

	public void clear() {
		nationalityStatus = "0";
		nationalityCountry.clear();
	}
	
//	public boolean isEqual(Nationality nationality) {
//		return StringUtils.equals(nationality.nationalityStatus, this.nationalityStatus) && //
//			sameValues(nationality.nationalityCountry, this.nationalityCountry);
//	}
}
