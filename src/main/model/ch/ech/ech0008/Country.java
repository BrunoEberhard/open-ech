package ch.ech.ech0008;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.View;

import ch.ech.ech0072.CountryInformation;

public class Country implements View<CountryInformation>, Code, Rendering {
	public static final Country $ = Keys.of(Country.class);

	public Integer id;
	public String iso2Id;
	public String shortNameDe;

	public Integer getCountryId() {
		return id;
	}

	public void setCountryId(Integer countryId) {
		this.id = countryId;
	}

	public String getCountryIdISO2() {
		return iso2Id;
	}

	public void setCountryIdISO2(String countryIdISO2) {
		this.iso2Id = countryIdISO2;
	}

	public String getCountryNameShort() {
		return shortNameDe;
	}

	public void setCountryNameShort(String countryNameShort) {
		this.shortNameDe = countryNameShort;
	}

	@Override
	public CharSequence render() {
		return shortNameDe;
	}

}