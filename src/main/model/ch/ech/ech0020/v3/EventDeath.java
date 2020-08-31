package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.823333700")
public class EventDeath {
	public static final EventDeath $ = Keys.of(EventDeath.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification deathPerson;
	public final ch.ech.ech0011.DeathData deathData = new ch.ech.ech0011.DeathData();
}