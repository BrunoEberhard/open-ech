package ch.ech.ech0020.v3;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.815337200")
public class SwissNationality {
	public static final SwissNationality $ = Keys.of(SwissNationality.class);

	public final String nationalityStatus = "2";
	public static class Country {
		public static final Country $ = Keys.of(Country.class);

		public final String countryId = "8100";
		public final String countryIdISO2 = "CH";
	}
	public final Country country = new Country();
}