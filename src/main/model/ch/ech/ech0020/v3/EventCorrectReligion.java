package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.840335400")
public class EventCorrectReligion {
	public static final EventCorrectReligion $ = Keys.of(EventCorrectReligion.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctReligionPerson;
	public final ch.ech.ech0011.ReligionData religionData = new ch.ech.ech0011.ReligionData();
}