package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventGuardianMeasure {
	public static final EventGuardianMeasure $ = Keys.of(EventGuardianMeasure.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification guardianMeasurePerson;
	public final ch.ech.ech0021.GuardianRelationship relationship = new ch.ech.ech0021.GuardianRelationship();
}