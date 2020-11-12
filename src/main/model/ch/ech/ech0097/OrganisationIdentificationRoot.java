package ch.ech.ech0097;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class OrganisationIdentificationRoot {
	public static final OrganisationIdentificationRoot $ = Keys.of(OrganisationIdentificationRoot.class);

	@NotEmpty
	public OrganisationIdentification organisationIdentification;
}