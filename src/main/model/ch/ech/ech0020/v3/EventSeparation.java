package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.820347300")
public class EventSeparation {
	public static final EventSeparation $ = Keys.of(EventSeparation.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification separationPerson;
	public final ch.ech.ech0011.SeparationData separationData = new ch.ech.ech0011.SeparationData();
}