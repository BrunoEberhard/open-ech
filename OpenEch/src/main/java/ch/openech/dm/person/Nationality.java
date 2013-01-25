package ch.openech.dm.person;

import ch.openech.dm.code.NationalityStatus;
import ch.openech.dm.common.CountryIdentification;
import ch.openech.dm.common.Swiss;
import ch.openech.mj.model.Constants;

public class Nationality  {

	public static final Nationality NATIONALITY = Constants.of(Nationality.class);
	
	public NationalityStatus nationalityStatus = NationalityStatus.with;
	public final CountryIdentification nationalityCountry = Swiss.createCountryIdentification();
	
	public Nationality() {
		// do nothing
	}
	
	private Nationality(NationalityStatus nationalityStatus) {
		this.nationalityStatus = nationalityStatus;
	}
	
	public static final Nationality newUnknown() {
		return new Nationality(NationalityStatus.unknown);
	}

	public static final Nationality newWithout() {
		return new Nationality(NationalityStatus.without);
	}

	public boolean isSwiss() {
		return nationalityStatus == NationalityStatus.with && nationalityCountry.isSwiss();
	}

	public void copyTo(Nationality copy) {
		copy.nationalityStatus = nationalityStatus;
		nationalityCountry.copyTo(copy.nationalityCountry);
	}

	@Override
	public int hashCode() {
		if (nationalityStatus == NationalityStatus.with) {
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
		if (nationalityStatus != other.nationalityStatus) {
			return false;
		}
		if (nationalityStatus == NationalityStatus.with) {
			return other.nationalityCountry.equals(nationalityCountry);
		} else {
			return true;
		}
	}

	@Override
	public String toString() {
		if (nationalityStatus == NationalityStatus.without) {
			return "Staatenlos";
		} else if (nationalityStatus == NationalityStatus.unknown) {
			return "Staatsangehörigkeit unbekannt";
		} else if (nationalityStatus == NationalityStatus.with) {
			return nationalityCountry.toString();
		} else {
			return nationalityStatus + " / " + nationalityCountry.toString();
		}
	}

	public void clear() {
		nationalityStatus = NationalityStatus.unknown;
		nationalityCountry.clear();
	}
	
}
