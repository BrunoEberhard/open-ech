package ch.openech.dm.person;

import java.util.ArrayList;
import java.util.List;

import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.db.model.Constants;

public class Residence {

	public static final Residence RESIDENCE = Constants.of(Residence.class);
	
	public MunicipalityIdentification reportingMunicipality;
	public final List<MunicipalityIdentification> secondary = new ArrayList<MunicipalityIdentification>(); // nur bei hasMainResidence

	@Override
	public Residence clone() {
		Residence clone = new Residence();
		if (reportingMunicipality != null) {
			clone.reportingMunicipality = this.reportingMunicipality.clone();
		}
		clone.secondary.clear();
		for (MunicipalityIdentification m : secondary) {
			clone.secondary.add(m.clone());
		}
		return clone;
	}

	public void setSecondary(List<MunicipalityIdentification> secondary2) {
		secondary.clear();
		secondary.addAll(secondary2);
	}
}
