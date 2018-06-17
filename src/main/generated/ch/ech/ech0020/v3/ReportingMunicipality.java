package ch.ech.ech0020.v3;

import java.time.LocalDate;
import java.util.List;

import org.minimalj.model.Keys;

// Handmade
public class ReportingMunicipality {
	public static final ReportingMunicipality $ = Keys.of(ReportingMunicipality.class);

	public ch.ech.ech0007.v4.SwissMunicipality reportingMunicipality;
	public ch.ech.ech0020.v2.FederalRegister federalRegister;
	public LocalDate arrivalDate;
	public ch.ech.ech0011.v8.Destination comesFrom;
	public ch.ech.ech0011.v8.DwellingAddress dwellingAddress;
	public LocalDate departureDate;
	public ch.ech.ech0011.v8.Destination goesTo;
	
	//

	public final ch.ech.ech0007.v4.SwissMunicipality mainResidence = new ch.ech.ech0007.v4.SwissMunicipality();
	public List<ch.ech.ech0007.v4.SwissMunicipality> secondaryResidence;

}