package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.831334300")
public class EventChangeNationality {
	public static final EventChangeNationality $ = Keys.of(EventChangeNationality.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification changeNationalityPerson;
	public final ch.ech.ech0011.NationalityData nationalityData = new ch.ech.ech0011.NationalityData();
}