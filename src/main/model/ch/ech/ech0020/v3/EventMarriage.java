package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.702")
public class EventMarriage {
	public static final EventMarriage $ = Keys.of(EventMarriage.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification marriagePerson;
	public final MaritalInfoRestrictedMarriage maritalInfo = new MaritalInfoRestrictedMarriage();
	public ch.ech.ech0021.MaritalRelationship maritalRelationship;
}