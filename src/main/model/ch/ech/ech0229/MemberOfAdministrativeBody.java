package ch.ech.ech0229;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.416")
public class MemberOfAdministrativeBody {
	public static final MemberOfAdministrativeBody $ = Keys.of(MemberOfAdministrativeBody.class);

	public ch.ech.ech0097.OrganisationIdentification organisationIdentification;
	public ch.ech.ech0044.PersonIdentification personIdentification;
	public ch.ech.ech0046.Contact contactInformation;
	public CantonExtension cantonExtension;
	@NotEmpty
	public AdministrativeBodyRole role;
}