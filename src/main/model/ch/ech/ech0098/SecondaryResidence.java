package ch.ech.ech0098;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class SecondaryResidence {
	public static final SecondaryResidence $ = Keys.of(SecondaryResidence.class);

	public SwissHeadquarter swissHeadquarter;
	public final ch.ech.ech0007.SwissMunicipality reportingMunicipality = new ch.ech.ech0007.SwissMunicipality();
	@NotEmpty
	public LocalDate arrivalDate;
	public ch.ech.ech0011.Destination comesFrom;
	public final DwellingAddress businessAddress = new DwellingAddress();
	public LocalDate departureDate;
	public ch.ech.ech0011.Destination goesTo;
}