package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventMarriage {
	public static final EventMarriage $ = Keys.of(EventMarriage.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification marriagePerson;
	public final MaritalInfoRestrictedMarriage maritalInfo = new MaritalInfoRestrictedMarriage();
	public ch.ech.ech0021.MaritalRelationship maritalRelationship;
}