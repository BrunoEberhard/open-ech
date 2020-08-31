package ch.ech.ech0011;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.762287200")
public class NationalityData {
	public static final NationalityData $ = Keys.of(NationalityData.class);

	@NotEmpty
	public NationalityStatus nationalityStatus;
	public static class CountryInfo {
		public static final CountryInfo $ = Keys.of(CountryInfo.class);

		@NotEmpty
		public ch.ech.ech0008.Country country;
		public LocalDate nationalityValidFrom;
	}
	public List<CountryInfo> countryInfo;
}