package ch.ech.ech0011;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.296")
public class Birthplace {
	public static final Birthplace $ = Keys.of(Birthplace.class);

	public Unknown unknown;
	public static class SwissTown {
		public static final SwissTown $ = Keys.of(SwissTown.class);

		@NotEmpty
		public ch.ech.ech0008.Country country;
		public final ch.ech.ech0007.SwissMunicipality municipality = new ch.ech.ech0007.SwissMunicipality();
	}
	public SwissTown swissTown;
	public static class ForeignCountry {
		public static final ForeignCountry $ = Keys.of(ForeignCountry.class);

		@NotEmpty
		public ch.ech.ech0008.Country country;
		@Size(100)
		public String foreignBirthTown;
	}
	public ForeignCountry foreignCountry;
}