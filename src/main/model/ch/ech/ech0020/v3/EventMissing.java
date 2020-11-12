package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventMissing {
	public static final EventMissing $ = Keys.of(EventMissing.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification missingPerson;
	public final ch.ech.ech0011.DeathData deathData = new ch.ech.ech0011.DeathData();
}