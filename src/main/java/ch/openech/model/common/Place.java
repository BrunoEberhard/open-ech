package  ch.openech.model.common;

import java.util.Locale;

import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Size;

import ch.openech.model.EchFormats;

// Verwendung als Birthplace und als Destination
public class Place implements Rendering {
	
	public MunicipalityIdentification municipalityIdentification;
	public CountryIdentification countryIdentification = CountryIdentification.createSwiss();
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

	@Override
	public String render(RenderType renderType) {
		if (isSwiss()) {
			return municipalityIdentification != null ? municipalityIdentification.toString() : "-";
		} else if (isForeign()) {
			String text = countryIdentification != null ? countryIdentification.toString() : "";
			if (foreignTown != null) {
				text = text + ", " + foreignTown;
			}
			return text;
		} else {
			return "-";
		}
	}

}
