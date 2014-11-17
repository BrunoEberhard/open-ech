package  ch.openech.model.common;

import java.io.Serializable;
import java.util.Locale;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Code;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.code.FederalRegister;

@Sizes(EchFormats.class)
public class MunicipalityIdentification implements Code, Rendering, Comparable<MunicipalityIdentification>, Serializable {
	private static final long serialVersionUID = 1L;

	public static MunicipalityIdentification $ = Keys.of(MunicipalityIdentification.class);

	@Size(4)
	public Integer id;
	public String municipalityName;
	public Canton canton;
	@Size(4)
	public Integer historyMunicipalityId;
	
	public void clear() {
		id = 0;
		municipalityName = null;
		canton = null;
		historyMunicipalityId = 0;
	}
	
	public boolean isEmpty() {
		return StringUtils.isBlank(municipalityName) && !isFederalRegister();
	}
	
	public boolean isFederalRegister() {
		return id != null && id < 0;
	}
	
	public FederalRegister getFederalRegister() {
		if (isFederalRegister()) {
			return FederalRegister.values()[-1-id];
		} else {
			return null;
		}
	}
	
	@Override
	public String render(RenderType renderType, Locale locale) {
		return formatMunicipality(municipalityName, canton);
	}

	public static String formatMunicipality(String municipalityName, Canton canton) {
		if (canton == null) return municipalityName;
		String cantonString = " (" + canton.id + ")";
		if (canton == null || municipalityName.endsWith(cantonString)) {
			return municipalityName;
		} else {
			return municipalityName + cantonString;
		}
	}
	
	@Override
	public int compareTo(MunicipalityIdentification m) {
		// Used in ComboBox
		int result = municipalityName.compareTo(m.municipalityName);
		if (result != 0) return result;
		else return canton.compareTo(m.canton);
	}

	public void copyTo(MunicipalityIdentification municipalityIdentification) {
		municipalityIdentification.id = this.id;
		municipalityIdentification.municipalityName = this.municipalityName;
		municipalityIdentification.canton = this.canton;
		municipalityIdentification.historyMunicipalityId = this.historyMunicipalityId;
	}

	// hash / equals. This is important

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		} else if (municipalityName != null) {
			return municipalityName.hashCode();
		} else {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((canton == null) ? 0 : canton.hashCode());
			result = prime * result + ((historyMunicipalityId == null) ? 0 : historyMunicipalityId.hashCode());
			return result;			
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof MunicipalityIdentification))
			return false;
		
		MunicipalityIdentification other = (MunicipalityIdentification) obj;
		
		// if id is the same other values don't matter
		if (id != null && other.id != null) {
			return id.equals(other.id);
		}
		
		if (municipalityName != null && other.municipalityName != null) {
			return municipalityName.equals(other.municipalityName);
		}
		
		return false;
	}

}
