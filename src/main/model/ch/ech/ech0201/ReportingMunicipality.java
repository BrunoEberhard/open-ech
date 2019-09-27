package ch.ech.ech0201;

import java.time.LocalDate;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.823")
public class ReportingMunicipality {
	public static final ReportingMunicipality $ = Keys.of(ReportingMunicipality.class);

	public final ch.ech.ech0007.SwissMunicipality reportingMunicipality = new ch.ech.ech0007.SwissMunicipality();
	public LocalDate arrivalDate;
	public final ch.ech.ech0011.DwellingAddress dwellingAddress = new ch.ech.ech0011.DwellingAddress();
	public LocalDate departureDate;
}