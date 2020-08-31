package ch.ech.ech0229;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.486260200")
public class Representative {
	public static final Representative $ = Keys.of(Representative.class);

	public ch.ech.ech0097.OrganisationIdentification organisationIdentification;
	public ch.ech.ech0044.PersonIdentification personIdentification;
	public ch.ech.ech0046.Contact contactInformation;
	public CantonExtension cantonExtension;
}