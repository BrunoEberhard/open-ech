package ch.ech.ech0071.v1;

import java.time.LocalDate;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

//handmade
public class Municipality implements Code, Rendering, Comparable<Municipality> {
	public static final Municipality $ = Keys.of(Municipality.class);

	@Size(5)
	public Integer id;
	@Size(5)
	public Integer districtHistId;
	public CantonAbbreviation cantonAbbreviation;
	@Size(4)
	public Integer municipalityId;
	@Size(60)
	public String municipalityLongName;
	@Size(24)
	public String municipalityShortName;
	@Size(2)
	public Integer municipalityEntryMode;
	@Size(1)
	public Integer municipalityStatus;
	@Size(4)
	public Integer municipalityAdmissionNumber;
	public AdmissionMode municipalityAdmissionMode;
	public LocalDate municipalityAdmissionDate;
	@Size(4)
	public Integer municipalityAbolitionNumber;
	public AbolitionMode municipalityAbolitionMode;
	public LocalDate municipalityAbolitionDate;
	public LocalDate municipalityDateOfChange;

	public Integer getHistoryMunicipalityId() {
		return id;
	}

	public void setHistoryMunicipalityId(Integer historyMunicipalityId) {
		this.id = historyMunicipalityId;
	}
	
	@Override
	public String render(RenderType renderType) {
		return municipalityShortName;
	}

	@Override
	public int compareTo(Municipality o) {
		return StringUtils.compare(render(RenderType.PLAIN_TEXT), o.render(RenderType.PLAIN_TEXT));
	}

}