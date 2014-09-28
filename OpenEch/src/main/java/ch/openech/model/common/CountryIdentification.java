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
public class CountryIdentification implements Code, Comparable<CountryIdentification>, Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	public static final CountryIdentification COUNTRY_IDENTIFICATION = Keys.of(CountryIdentification.class);

	public static final Integer SWISS_COUNTRY_ID = 8100;
	public static final String SWISS_COUNTRY_ISO2 = "CH";
	public static final String SWISS_COUNTRY_NAME_SHORT = "Schweiz";
	
	@Size(4)
	public Integer id;
	public String countryIdISO2;
	@Required
	public String countryNameShort;

	public String toStringReadable() {
		StringBuilder s = new StringBuilder();
		s.append("Name Land = " + countryNameShort);
		s.append(", Iso Code = " + countryIdISO2);
		s.append(", Id = " + id);
		return s.toString();
	}

	public boolean isEmpty() {
		return id == null || id == 0;
	}

	public void clear() {
		id = null;
		countryIdISO2 = countryNameShort = null;
	}

	public static CountryIdentification createSwiss() {
		CountryIdentification countryIdentification = new CountryIdentification();
		countryIdentification.id = SWISS_COUNTRY_ID;
		countryIdentification.countryIdISO2 = SWISS_COUNTRY_ISO2;
		countryIdentification.countryNameShort = SWISS_COUNTRY_NAME_SHORT;
		return countryIdentification;
	}
	
	public boolean isSwiss() {
		return SWISS_COUNTRY_ID.equals(id) || //
				SWISS_COUNTRY_ISO2.equals(countryIdISO2) || //
				SWISS_COUNTRY_NAME_SHORT.equals(countryNameShort);
	}

	@Override
	public String toString() {
		// Used in ComboBox - Renderer
		return countryNameShort;
	}

	@Override
//	public String getText(Locale local) {
	public String display() {
		return countryNameShort;
	}
	
	@Override
	public int compareTo(CountryIdentification c) {
		// Used in ComboBox to sort
		return StringUtils.compare(toString(), c.toString());
	}

	// hash / equals. This is important

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		} else if (countryIdISO2 != null) {
			return countryIdISO2.hashCode();
		} else if (countryNameShort != null) {
			return countryNameShort.hashCode();
		} else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CountryIdentification))
			return false;
		
		CountryIdentification other = (CountryIdentification) obj;
		
		// if id is the same other values don't matter
		if (id != null && other.id != null) {
			return id.equals(other.id);
		}
		
		if (countryIdISO2 != null && other.countryIdISO2 != null) {
			return countryIdISO2.equals(other.countryIdISO2);
		}
		
		if (countryNameShort != null && other.countryNameShort != null) {
			return countryNameShort.equals(other.countryNameShort);
		}
		
		return false;
	}

}
