package ch.openech.model.person;

import ch.openech.model.common.MunicipalityIdentification;

public class SecondaryResidence {

	public SecondaryResidence() {
		// nothing
	}
	
	public SecondaryResidence(MunicipalityIdentification municipalityIdentification) {
		this.municipalityIdentification = municipalityIdentification;
	}
	
	public MunicipalityIdentification municipalityIdentification;
}
