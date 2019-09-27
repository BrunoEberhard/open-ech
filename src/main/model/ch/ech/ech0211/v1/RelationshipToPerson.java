package ch.ech.ech0211.v1;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.994")
public class RelationshipToPerson {
	public static final RelationshipToPerson $ = Keys.of(RelationshipToPerson.class);

	@NotEmpty
	@Size(100)
	public String role;
	public ch.ech.ech0044.PersonIdentification personIdentification;
	public ch.ech.ech0097.OrganisationIdentification organisationIdentification;
	public final ch.ech.ech0010.MailAddress address = new ch.ech.ech0010.MailAddress();
	public ch.ech.ech0046.Email email;
	public ch.ech.ech0046.Phone phone;
	public ch.ech.ech0010.MailAddress deputy;
}