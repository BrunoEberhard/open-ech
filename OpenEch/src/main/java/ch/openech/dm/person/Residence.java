package ch.openech.dm.person;

import java.util.ArrayList;
import java.util.List;

import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.db.model.Constants;

public class Residence {

	public static final Residence RESIDENCE = Constants.of(Residence.class);
	
	public MunicipalityIdentification reportingMunicipality;
	public final List<MunicipalityIdentification> secondary = new ArrayList<MunicipalityIdentification>(); // nur bei hasMainResidence

	public void setSecondary(List<MunicipalityIdentification> secondary2) {
		secondary.clear();
		secondary.addAll(secondary2);
	}
}
