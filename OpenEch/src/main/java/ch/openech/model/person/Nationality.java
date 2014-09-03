package  ch.openech.model.person;

import org.minimalj.model.Keys;

import ch.openech.model.code.NationalityStatus;
import ch.openech.model.common.CountryIdentification;

public class Nationality  {

	public static final Nationality NATIONALITY = Keys.of(Nationality.class);
	
	public NationalityStatus nationalityStatus = NationalityStatus.with;
	public CountryIdentification nationalityCountry;
	
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
		return nationalityStatus == NationalityStatus.with && nationalityCountry != null && nationalityCountry.isSwiss();
	}

	public void copyTo(Nationality copy) {
		copy.nationalityStatus = nationalityStatus;
		copy.nationalityCountry = nationalityCountry;
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
		nationalityCountry = null;
	}
	
}
