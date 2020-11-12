package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventCorrectNationality {
	public static final EventCorrectNationality $ = Keys.of(EventCorrectNationality.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctNationalityPerson;
	public final ch.ech.ech0011.NationalityData nationalityData = new ch.ech.ech0011.NationalityData();
}