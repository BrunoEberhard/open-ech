package ch.ech.ech0011;

import org.minimalj.model.Keys;

import ch.ech.ech0011.GeneralPlace.ForeignCountry;

// handmade: GeneralPlace soll wiederverwendet werden
public class Destination {
	public static final Destination $ = Keys.of(Destination.class);

	public final GeneralPlace generalPlace = new GeneralPlace();
	public ch.ech.ech0010.AddressInformation mailAddress;

	public Unknown getUnknown() {
		return generalPlace.unknown;
	}

	public void setUnknown(Unknown unknown) {
		generalPlace.unknown = unknown;
	}

	public ch.ech.ech0007.SwissMunicipality getSwissTown() {
		return generalPlace.swissTown;
	}

	public void setSwissTown(ch.ech.ech0007.SwissMunicipality swissTown) {
		generalPlace.swissTown = swissTown;
	}

	public ForeignCountry getForeignCountry() {
		return generalPlace.foreignCountry;
	}

	public void setForeignCountry(ForeignCountry foreignCountry) {
		generalPlace.foreignCountry = foreignCountry;
	}

}