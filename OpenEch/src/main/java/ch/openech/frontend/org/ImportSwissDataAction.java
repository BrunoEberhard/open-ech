package ch.openech.frontend.org;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.toolkit.ResourceAction;
import org.minimalj.model.EnumUtils;

import ch.openech.model.code.FederalRegister;
import ch.openech.model.common.Canton;
import ch.openech.model.common.CountryIdentification;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.xml.read.StaxEch0071;
import ch.openech.xml.read.StaxEch0072;

public class ImportSwissDataAction extends ResourceAction {

	public ImportSwissDataAction() {
	}

	@Override
	public void action() {
		for (CountryIdentification country : StaxEch0072.getInstance().getCountryIdentifications()) {
			Backend.getInstance().insert(country);
		}
		
		for (Canton canton : StaxEch0071.getInstance().getCantons()) {
			Backend.getInstance().insert(canton);
		}

		for (MunicipalityIdentification municipality : StaxEch0071.getInstance().getMunicipalityIdentifications()) {
			Backend.getInstance().insert(municipality);
		}
		
		int pos = 0;
		for (FederalRegister federalRegister : FederalRegister.values()) {
			MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
			municipalityIdentification.id = --pos;
			municipalityIdentification.municipalityName = EnumUtils.getText(federalRegister);
			Backend.getInstance().insert(municipalityIdentification);
		}

//		if (inputStream != null) {
//			Backend.getInstance().execute(new OrganisationImportStreamConsumer(), inputStream);
//		}
	}
	
}
