package ch.openech.dm.common;

import ch.openech.dm.EchFormats;
import ch.openech.mj.edit.value.Reference;
import ch.openech.mj.model.annotation.Size;

// Verwendung als Birthplace und als Destination
public class Place {
	
	@Reference public final MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
	@Reference public final CountryIdentification countryIdentification = Swiss.createCountryIdentification();
	@Size(EchFormats.baseName)  // TODO REMOVE
	public String foreignTown;
	public Address mailAddress; // nur bei Verwendung als Destination
	
	//

	public void clear() {
		municipalityIdentification.clear();
		countryIdentification.clear();
		foreignTown = null;
		mailAddress = null;
	}	
	
	public void setCountryIdentification(CountryIdentification countryIdentification) {
		countryIdentification.copyTo(this.countryIdentification);
	}

	public void setMunicipalityIdentification(MunicipalityIdentification municipalityIdentification) {
		municipalityIdentification.copyTo(this.municipalityIdentification);
	}

	public boolean isSwiss() {
		return countryIdentification.isSwiss();
	}

	public boolean isForeign() {
		return !countryIdentification.isEmpty() && !countryIdentification.isSwiss();
	}

	public boolean isUnknown() {
		return !(isSwiss() && !municipalityIdentification.isEmpty() || isForeign());
	}

}
