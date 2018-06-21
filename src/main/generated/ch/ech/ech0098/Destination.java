package ch.ech.ech0098;

import org.minimalj.model.Keys;

import ch.ech.ech0011.GeneralPlace;

// hand made
public class Destination {
	public static final Destination $ = Keys.of(Destination.class);

	public Unknown unknown;
	public ch.ech.ech0007.SwissMunicipality swissTown;
	public GeneralPlace.ForeignCountry foreignCountry; // !
	public ch.ech.ech0010.AddressInformation mailAddress;
}