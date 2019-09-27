package ch.ech.ech0021;

import java.time.LocalDate;

import org.minimalj.model.Keys;

// handmade, delegate Methoden für PlaceOfOrigin
public class PlaceOfOriginAddon {
	public static final PlaceOfOriginAddon $ = Keys.of(PlaceOfOriginAddon.class);

	public final ch.ech.ech0011.PlaceOfOrigin origin = new ch.ech.ech0011.PlaceOfOrigin();

	public enum ReasonOfAcquisition { _1, _2, _3, _4, _5, _6, _8, _9, _10, _11;	}
	public ReasonOfAcquisition reasonOfAcquisition;
	public LocalDate naturalizationDate;
	public LocalDate expatriationDate;

	public String getOriginName() {
		return origin.originName;
	}

	public void setOriginName(String originName) {
		origin.originName = originName;
	}

	public ch.ech.ech0071.CantonAbbreviation getCanton() {
		return origin.canton;
	}

	public void setCanton(ch.ech.ech0071.CantonAbbreviation canton) {
		origin.canton = canton;
	}

	public Integer getPlaceOfOriginId() {
		return origin.placeOfOriginId;
	}

	public void setPlaceOfOriginId(Integer placeOfOriginId) {
		origin.placeOfOriginId = placeOfOriginId;
	}

	public Integer getHistoryMunicipalityId() {
		return origin.historyMunicipalityId;
	}

	public void setHistoryMunicipalityId(Integer historyMunicipalityId) {
		origin.historyMunicipalityId = historyMunicipalityId;
	}

}