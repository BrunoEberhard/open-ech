package ch.ech.ech0011;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.repository.sql.EmptyObjects;
import org.minimalj.util.StringUtils;

// rendering
public class GeneralPlace implements Rendering {
	public static final GeneralPlace $ = Keys.of(GeneralPlace.class);

	public Unknown unknown;
	public ch.ech.ech0007.SwissMunicipality swissTown;
	public static class ForeignCountry {
		public static final ForeignCountry $ = Keys.of(ForeignCountry.class);

		@NotEmpty
		public ch.ech.ech0008.Country country;
		@Size(100)
		public String town;
	}
	public ForeignCountry foreignCountry;

	@Override
	public CharSequence render() {
		if (!EmptyObjects.isEmpty(swissTown)) {
			return swissTown.municipalityShortName;
		} else if (!EmptyObjects.isEmpty(foreignCountry)) {
			StringBuilder s = new StringBuilder();
			if (!StringUtils.isBlank(foreignCountry.town)) {
				s.append(foreignCountry.town);
			}
			if (foreignCountry.country != null) {
				if (s.length() > 0) {
					s.append(", ");
				}
				s.append(foreignCountry.country.shortNameDe);
			}
			return s.toString();
		} else {
			return "Unbekannt";
		}
	}
}