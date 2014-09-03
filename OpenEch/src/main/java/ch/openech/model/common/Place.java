package  ch.openech.model.common;

import org.minimalj.model.annotation.Size;

import ch.openech.model.EchFormats;

// Verwendung als Birthplace und als Destination
public class Place {
	
	public MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
	public CountryIdentification countryIdentification = Swiss.createCountryIdentification();
	@Size(EchFormats.baseName)  // TODO REMOVE
	public String foreignTown;
	public Address mailAddress; // nur bei Verwendung als Destination
	
	//

	public boolean isSwiss() {
		return countryIdentification != null && countryIdentification.isSwiss();
	}

	public boolean isForeign() {
		return countryIdentification != null && !countryIdentification.isEmpty() && !countryIdentification.isSwiss();
	}

	public boolean isUnknown() {
		return !(isSwiss() && municipalityIdentification != null && !municipalityIdentification.isEmpty()|| isForeign());
	}

}
