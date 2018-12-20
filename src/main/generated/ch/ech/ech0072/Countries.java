package ch.ech.ech0072;

import java.time.LocalDate;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

// handmade das country Feld würde zu einem ech0008 country, weil es in predefined types definiert
// ist. Das stimmt hier nicht, hier muss es wirklich ein CountryInformation sein.
public class Countries {
	public static final Countries $ = Keys.of(Countries.class);

	public Object id;
	@NotEmpty
	public LocalDate validFrom;
	@NotEmpty
	public List<CountryInformation> country;
}