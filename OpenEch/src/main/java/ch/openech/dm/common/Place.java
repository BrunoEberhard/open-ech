package ch.openech.dm.common;

import ch.openech.dm.EchFormats;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.edit.value.Reference;

// Verwendung als Birthplace und als Destination
public class Place {
	
	@Reference public final MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
	@Reference public final CountryIdentification countryIdentification = Swiss.createCountryIdentification();
	@FormatName(EchFormats.baseName) // TODO REMOVE
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

	
//	@Override
//	protected Place clone() {
//		Place clone = new Place();
//		clone.municipalityIdentification = this.municipalityIdentification.clone();
//		clone.countryIdentification = this.countryIdentification.clone();
//		clone.foreignTown = this.foreignTown;
//		if (this.mailAddress != null) {
//			clone.mailAddress = this.mailAddress.clone();
//		}
//		return clone;
//	}

	public boolean isSwiss() {
		return countryIdentification.isSwiss();
	}

	public boolean isForeign() {
		return !countryIdentification.isEmpty() && !countryIdentification.isSwiss();
	}

	public boolean isUnknown() {
		return !(isSwiss() && !municipalityIdentification.isEmpty() || isForeign());
	}

//	@Override
//	public void copyTo(Map<String, String> valueMap) {
//		super.copyTo(valueMap);
//		if (valueMap instanceof Place) {
//			Place place = (Place) valueMap;
//			municipalityIdentification.copyTo(place.municipalityIdentification);
//			countryIdentification.copyTo(place.countryIdentification);
//			if (mailAddress != null) {
//				if (place.mailAddress == null) place.mailAddress = new Address();
//				mailAddress.copyTo(place.mailAddress);
//			}
//		}
//	}

}
