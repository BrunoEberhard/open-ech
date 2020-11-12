package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventCorrectOccupation {
	public static final EventCorrectOccupation $ = Keys.of(EventCorrectOccupation.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctOccupationPerson;
	public ch.ech.ech0021.JobData jobData;
}