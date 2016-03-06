package ch.openech.frontend.org;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.model.EnumUtils;

import ch.openech.model.code.FederalRegister;
import ch.openech.model.common.Canton;
import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.xml.read.StaxEch0071;
import ch.openech.xml.read.StaxEch0072;

public class ImportSwissDataAction extends Action {

	public ImportSwissDataAction() {
	}

	@Override
	public void action() {
		StaxEch0072 staxEch0072 = new StaxEch0072();
		for (CountryIdentification country : staxEch0072.getCountryIdentifications()) {
			Backend.insert(country);
		}
		
		StaxEch0071 staxEch0071 = new StaxEch0071();
		for (Canton canton : staxEch0071.getCantons()) {
			Backend.insert(canton);
		}

		for (MunicipalityIdentification municipality : staxEch0071.getMunicipalityIdentifications()) {
			Backend.insert(municipality);
		}
		
		int pos = 0;
		for (FederalRegister federalRegister : FederalRegister.values()) {
			MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
			municipalityIdentification.id = --pos;
			municipalityIdentification.municipalityName = EnumUtils.getText(federalRegister);
			Backend.insert(municipalityIdentification);
		}
	}
	
}
