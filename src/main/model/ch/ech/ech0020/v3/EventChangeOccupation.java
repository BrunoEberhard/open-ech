package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.889")
public class EventChangeOccupation {
	public static final EventChangeOccupation $ = Keys.of(EventChangeOccupation.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification changeOccupationPerson;
	public ch.ech.ech0021.JobData jobData;
}