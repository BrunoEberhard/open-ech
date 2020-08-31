package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.829333200")
public class EventChangeReligion {
	public static final EventChangeReligion $ = Keys.of(EventChangeReligion.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification changeReligionPerson;
	public final ch.ech.ech0011.ReligionData religionData = new ch.ech.ech0011.ReligionData();
}