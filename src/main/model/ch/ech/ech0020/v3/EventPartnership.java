package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.820347300")
public class EventPartnership {
	public static final EventPartnership $ = Keys.of(EventPartnership.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification partnershipPerson;
	public final MaritalInfoRestrictedMarriage maritalInfo = new MaritalInfoRestrictedMarriage();
	public ch.ech.ech0021.MaritalRelationship partnershipRelationship;
}