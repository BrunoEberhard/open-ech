package ch.ech.ech0007;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.View;

import ch.ech.ech0071.Municipality;

public class SwissMunicipality implements View<Municipality>, Code {
	public static final SwissMunicipality $ = Keys.of(SwissMunicipality.class);

	public Integer id;
	public Integer municipalityId;
	public String municipalityShortName;
	public ch.ech.ech0071.CantonAbbreviation cantonAbbreviation;
	public Integer historyMunicipalityId;

	public String getMunicipalityName() {
		return municipalityShortName;
	}

	public void setMunicipalityName(String municipalityShortName) {
		this.municipalityShortName = municipalityShortName;
	}

	// typo!! (piality)

	public Integer getHistoryMunicipialityId() {
		return historyMunicipalityId;
	}

	public void setHistoryMunicipialityId(Integer historyMunicipalityId) {
		this.historyMunicipalityId = historyMunicipalityId;
	}

}