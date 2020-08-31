package ch.ech.ech0129;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.338082800")
public class PersonOrOrganisation {
	public static final PersonOrOrganisation $ = Keys.of(PersonOrOrganisation.class);

	public ch.ech.ech0044.PersonIdentification individual;
	public ch.ech.ech0097.OrganisationIdentification organisation;
}