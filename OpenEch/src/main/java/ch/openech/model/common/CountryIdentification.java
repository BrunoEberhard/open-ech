package  ch.openech.model.common;

import java.io.Serializable;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Code;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;

@Sizes(EchFormats.class)
public class CountryIdentification implements Comparable<CountryIdentification>, Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	public static final CountryIdentification COUNTRY_IDENTIFICATION = Keys.of(CountryIdentification.class);

	@Size(4) @Code @Required
	public Integer countryId;
	public String countryIdISO2;
	@Required
	public String countryNameShort;

	public String toStringReadable() {
		StringBuilder s = new StringBuilder();
		s.append("Name Land = " + countryNameShort);
		s.append(", Iso Code = " + countryIdISO2);
		s.append(", Id = " + countryId);
		return s.toString();
	}

	public boolean isEmpty() {
		return countryId == null || countryId == 0;
	}

	public void clear() {
		countryId = null;
		countryIdISO2 = countryNameShort = null;
	}

	public void copyTo(CountryIdentification copy) {
		copy.countryId = countryId;
		copy.countryIdISO2 = countryIdISO2;
		copy.countryNameShort = countryNameShort;
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
		// Used in ComboBox to sort
		return StringUtils.compare(toString(), c.toString());
	}

	// Generated with eclipse

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((countryId == null) ? 0 : countryId.hashCode());
		result = prime * result
				+ ((countryIdISO2 == null) ? 0 : countryIdISO2.hashCode());
		result = prime
				* result
				+ ((countryNameShort == null) ? 0 : countryNameShort.hashCode());
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
