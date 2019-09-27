package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.764")
public class EventMissing {
	public static final EventMissing $ = Keys.of(EventMissing.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification missingPerson;
	public final ch.ech.ech0011.DeathData deathData = new ch.ech.ech0011.DeathData();
}