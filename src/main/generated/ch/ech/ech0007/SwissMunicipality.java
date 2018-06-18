package ch.ech.ech0007;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

// handmade
public class SwissMunicipality {
	public static final SwissMunicipality $ = Keys.of(SwissMunicipality.class);

	@Size(4)
	public Integer municipalityId;
	@NotEmpty
	@Size(40)
	public String municipalityName;
	public CantonAbbreviation cantonAbbreviation;
	@Size(5)
	public Integer historyMunicipalityId;
	
	//
	
	public Integer getHistoryMunicipialityId() {
		return historyMunicipalityId;
	}
	
	public void setHistoryMunicipialityId(Integer historyMunicipalityId) {
		this.historyMunicipalityId = historyMunicipalityId;
	}
}