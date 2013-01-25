package ch.openech.dm.common;

import java.io.Serializable;

import ch.openech.dm.EchFormats;
import ch.openech.dm.code.FederalRegister;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.annotation.Decimal;
import ch.openech.mj.model.annotation.Sizes;
import ch.openech.mj.util.StringUtils;

@Sizes(EchFormats.class)
public class MunicipalityIdentification implements Comparable<MunicipalityIdentification>, Serializable {

	public static MunicipalityIdentification MUNICIPALITY_IDENTIFICATION = Keys.of(MunicipalityIdentification.class);

	@Decimal(4)
	public Integer municipalityId;
	public String municipalityName;
	public final CantonAbbreviation cantonAbbreviation = new CantonAbbreviation();
	@Decimal(4)
	public Integer historyMunicipalityId;
	
	public void clear() {
		municipalityId = 0;
		municipalityName = null;
		cantonAbbreviation.canton = null;
		historyMunicipalityId = 0;
	}
	
	public boolean isEmpty() {
		return StringUtils.isBlank(municipalityName) && !isFederalRegister();
	}
	
	public boolean isFederalRegister() {
		return historyMunicipalityId != null && historyMunicipalityId < 0;
	}
	
	public FederalRegister getFederalRegister() {
		if (isFederalRegister()) {
			return FederalRegister.values()[-1-historyMunicipalityId];
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return formatMunicipality(municipalityName, cantonAbbreviation.canton);
	}
	
	public static String formatMunicipality(String municipalityName, String cantonAbbreviation) {
		String canton = " (" + cantonAbbreviation + ")";
		if (cantonAbbreviation == null || municipalityName.endsWith(canton)) {
			return municipalityName;
		} else {
			return municipalityName + canton;
		}
	}
	
	@Override
	public int compareTo(MunicipalityIdentification m) {
		// Used in ComboBox
		int result = municipalityName.compareTo(m.municipalityName);
		if (result != 0) return result;
		else return cantonAbbreviation.canton.compareTo(m.cantonAbbreviation.canton);
	}

	public void copyTo(MunicipalityIdentification municipalityIdentification) {
		municipalityIdentification.municipalityId = this.municipalityId;
		municipalityIdentification.municipalityName = this.municipalityName;
		municipalityIdentification.cantonAbbreviation.canton = this.cantonAbbreviation.canton;
		municipalityIdentification.historyMunicipalityId = this.historyMunicipalityId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cantonAbbreviation == null) ? 0 : cantonAbbreviation
						.hashCode());
		result = prime
				* result
				+ ((historyMunicipalityId == null) ? 0 : historyMunicipalityId
						.hashCode());
		result = prime * result
				+ ((municipalityId == null) ? 0 : municipalityId.hashCode());
		result = prime
				* result
				+ ((municipalityName == null) ? 0 : municipalityName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MunicipalityIdentification other = (MunicipalityIdentification) obj;
		if (cantonAbbreviation == null) {
			if (other.cantonAbbreviation != null)
				return false;
		} else if (!cantonAbbreviation.equals(other.cantonAbbreviation))
			return false;
		if (historyMunicipalityId == null) {
			if (other.historyMunicipalityId != null)
				return false;
		} else if (!historyMunicipalityId.equals(other.historyMunicipalityId))
			return false;
		if (municipalityId == null) {
			if (other.municipalityId != null)
				return false;
		} else if (!municipalityId.equals(other.municipalityId))
			return false;
		if (municipalityName == null) {
			if (other.municipalityName != null)
				return false;
		} else if (!municipalityName.equals(other.municipalityName))
			return false;
		return true;
	}

}
