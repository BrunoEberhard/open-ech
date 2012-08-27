package ch.openech.dm.common;

import java.io.Serializable;

import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Is;
import ch.openech.mj.util.StringUtils;

public class MunicipalityIdentification implements Comparable<MunicipalityIdentification>, Serializable {

	public static MunicipalityIdentification MUNICIPALITY_IDENTIFICATION = Constants.of(MunicipalityIdentification.class);

	public String municipalityId;
	public String municipalityName;
	public String cantonAbbreviation;
	@Is("municipalityId")
	public String historyMunicipalityId;
	
	public void clear() {
		municipalityId = null;
		municipalityName = null;
		cantonAbbreviation = null;
		historyMunicipalityId = null;
	}
	
	public boolean isEmpty() {
		return StringUtils.isBlank(municipalityName) && !isFederalRegister();
	}
	
	public boolean isFederalRegister() {
		if (StringUtils.isBlank(historyMunicipalityId)) return false;
		if (historyMunicipalityId.length() != 2) return false;
		if (historyMunicipalityId.charAt(0) != '-') return false;
		return true;
	}
	
	public String getFederalRegister() {
		if (!isFederalRegister()) return null;
		return historyMunicipalityId.substring(1,2);
	}
	
	@Override
	public String toString() {
		return formatMunicipality(municipalityName, cantonAbbreviation);
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
		else return cantonAbbreviation.compareTo(m.cantonAbbreviation);
	}

	public void copyTo(MunicipalityIdentification municipalityIdentification) {
		municipalityIdentification.municipalityId = this.municipalityId;
		municipalityIdentification.municipalityName = this.municipalityName;
		municipalityIdentification.cantonAbbreviation = this.cantonAbbreviation;
		municipalityIdentification.historyMunicipalityId = this.historyMunicipalityId;
	}
	
	@Override
	public int hashCode() {
		if (historyMunicipalityId != null) return historyMunicipalityId.hashCode();
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cantonAbbreviation == null) ? 0 : cantonAbbreviation.hashCode());
		result = prime * result + ((historyMunicipalityId == null) ? 0 : historyMunicipalityId.hashCode());
		result = prime * result + ((municipalityId == null) ? 0 : municipalityId.hashCode());
		result = prime * result + ((municipalityName == null) ? 0 : municipalityName.hashCode());
		return result;
	}

	// Generated with eclipse

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
