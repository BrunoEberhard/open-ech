package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventCorrectDeathData {
	public static final EventCorrectDeathData $ = Keys.of(EventCorrectDeathData.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctDeathDataPerson;
	public ch.ech.ech0011.DeathData deathData;
}