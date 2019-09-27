package ch.ech.ech0129;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.409")
public class PersonOrOrganisation {
	public static final PersonOrOrganisation $ = Keys.of(PersonOrOrganisation.class);

	public ch.ech.ech0044.PersonIdentification individual;
	public ch.ech.ech0097.OrganisationIdentification organisation;
}