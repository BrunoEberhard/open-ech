package ch.ech.ech0071.v1;

import java.time.LocalDate;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

//handmade
public class District implements Code {
	public static final District $ = Keys.of(District.class);

	@Size(5)
	public Integer id;
	@Size(2)
	public Integer cantonId;
	@Size(4)
	public Integer districtId;
	@Size(60)
	public String districtLongName;
	@Size(24)
	public String districtShortName;
	@Size(2)
	public Integer districtEntryMode;
	@Size(3)
	public Integer districtAdmissionNumber;
	public AdmissionMode districtAdmissionMode;
	public LocalDate districtAdmissionDate;
	@Size(3)
	public Integer districtAbolitionNumber;
	public AbolitionMode districtAbolitionMode;
	public LocalDate districtAbolitionDate;
	public LocalDate districtDateOfChange;

	public Integer getDistrictHistId() {
		return id;
	}

	public void setDistrictHistId(Integer districtHistId) {
		this.id = districtHistId;
	}

}