package ch.ech.ech0108;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.230")
public class OrganisationRoot {
	public static final OrganisationRoot $ = Keys.of(OrganisationRoot.class);

	@NotEmpty
	public Organisation organisation;
}