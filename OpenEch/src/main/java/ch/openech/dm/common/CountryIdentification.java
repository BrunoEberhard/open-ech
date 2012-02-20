package ch.openech.dm.common;

import java.io.Serializable;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.util.StringUtils;

public class CountryIdentification implements Comparable<CountryIdentification>, Serializable, Cloneable {

	public static final CountryIdentification COUNTRY_IDENTIFICATION = Constants.of(CountryIdentification.class);

	public String countryId;
	public String countryIdISO2;
	public String countryNameShort;

	public String toStringReadable() {
		StringBuilder s = new StringBuilder();
		s.append("Name Land = " + countryNameShort);
		s.append(", Iso Code = " + countryIdISO2);
		s.append(", Id = " + countryId);
		return s.toString();
	}

	public boolean isEmpty() {
		return StringUtils.isBlank(countryNameShort);
	}

	public void clear() {
		countryId = countryIdISO2 = countryNameShort = null;
	}

	public void copyTo(CountryIdentification copy) {
		copy.countryId = countryId;
		copy.countryIdISO2 = countryIdISO2;
		copy.countryNameShort = countryNameShort;
	}

	@Override
	public CountryIdentification clone() {
		CountryIdentification clone = new CountryIdentification();
		clone.countryId = countryId;
		clone.countryIdISO2 = countryIdISO2;
		clone.countryNameShort = countryNameShort;
		return clone;
	}

	public boolean isSwiss() {
		return Swiss.SWISS_COUNTRY_ID.equals(countryId) && //
				Swiss.SWISS_COUNTRY_ISO2.equals(countryIdISO2) && //
				Swiss.SWISS_COUNTRY_NAME_SHORT.equals(countryNameShort);
	}

	@Override
	public String toString() {
		// Used in ComboBox - Renderer
		return countryNameShort;
	}

	@Override
	public int compareTo(CountryIdentification c) {
		// Used in ComboBox
		return countryNameShort.compareTo(c.countryNameShort);
	}

	// Generated with eclipse

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
		result = prime * result + ((countryIdISO2 == null) ? 0 : countryIdISO2.hashCode());
		result = prime * result + ((countryNameShort == null) ? 0 : countryNameShort.hashCode());
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
		CountryIdentification other = (CountryIdentification) obj;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		if (countryIdISO2 == null) {
			if (other.countryIdISO2 != null)
				return false;
		} else if (!countryIdISO2.equals(other.countryIdISO2))
			return false;
		if (countryNameShort == null) {
			if (other.countryNameShort != null)
				return false;
		} else if (!countryNameShort.equals(other.countryNameShort))
			return false;
		return true;
	}

}
