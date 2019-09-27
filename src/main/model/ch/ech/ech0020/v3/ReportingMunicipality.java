package ch.ech.ech0020.v3;

import java.time.LocalDate;
import java.util.List;

import org.minimalj.model.Keys;

// Handmade
// ReportingMunicipalityRestrictedBaseMain: arrivalDate zwingend
// ReportingMunicipalityRestrictedBaseSecondary: arrivalDate, comesFrom zwingend
// ReportingMunicipalityRestrictedMoveIn: arrivalDate zwingend, kein departureDate, goesTo
// ReportingMunicipalityRestrictedMove: nur, aber zwingend: departureDate, goesTo 
// ReportingMunicipalityRestrictedMove: nur dwellingAddress
public class ReportingMunicipality {
	public static final ReportingMunicipality $ = Keys.of(ReportingMunicipality.class);

	public ch.ech.ech0007.SwissMunicipality reportingMunicipality;
	public FederalRegister federalRegister;
	public LocalDate arrivalDate;
	public ch.ech.ech0011.Destination comesFrom;
	public ch.ech.ech0011.DwellingAddress dwellingAddress;
	public LocalDate departureDate;
	public ch.ech.ech0011.Destination goesTo;
	
	//

	public final ch.ech.ech0007.SwissMunicipality mainResidence = new ch.ech.ech0007.SwissMunicipality();
	public List<ch.ech.ech0007.SwissMunicipality> secondaryResidence;

}