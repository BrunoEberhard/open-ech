package ch.ech.ech0108;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.324112800")
public class OrganisationRoot {
	public static final OrganisationRoot $ = Keys.of(OrganisationRoot.class);

	@NotEmpty
	public Organisation organisation;
}