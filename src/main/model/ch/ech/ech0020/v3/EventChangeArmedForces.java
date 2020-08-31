package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.845333600")
public class EventChangeArmedForces {
	public static final EventChangeArmedForces $ = Keys.of(EventChangeArmedForces.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification changeArmedForcesPerson;
	public ch.ech.ech0021.ArmedForcesData armedForcesData;
}