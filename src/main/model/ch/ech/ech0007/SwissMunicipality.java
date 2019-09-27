package ch.ech.ech0007;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.View;

import ch.ech.ech0071.Municipality;

public class SwissMunicipality implements View<Municipality>, Code, Rendering {
	public static final SwissMunicipality $ = Keys.of(SwissMunicipality.class);

	public Integer id;
	public Integer municipalityId;
	public String municipalityShortName;
	public ch.ech.ech0071.CantonAbbreviation cantonAbbreviation;

	// id wird auch bei Municipality as historyMunicipalityId verwendet.
	// daher auch hier. ein super.get ist nicht möglich, da es ja nicht
	// extends ist, sondern implements

	public Integer getHistoryMunicipalityId() {
		return id;
	}

	public void setHistoryMunicipalityId(Integer historyMunicipalityId) {
		this.id = historyMunicipalityId;
	}

	public String getMunicipalityName() {
		return municipalityShortName;
	}

	public void setMunicipalityName(String municipalityShortName) {
		this.municipalityShortName = municipalityShortName;
	}

	// typo!! (piality)

	public Integer getHistoryMunicipialityId() {
		return id;
	}

	public void setHistoryMunicipialityId(Integer historyMunicipalityId) {
		this.id = historyMunicipalityId;
	}

	@Override
	public CharSequence render() {
		return municipalityShortName;
	}

}