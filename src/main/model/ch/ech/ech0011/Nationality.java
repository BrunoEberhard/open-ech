package ch.ech.ech0011;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.202")
public class Nationality {
	public static final Nationality $ = Keys.of(Nationality.class);

	@NotEmpty
	public NationalityStatus nationalityStatus;
	public ch.ech.ech0008.Country country;
}