package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.824332700")
public class ChangeSexPerson {
	public static final ChangeSexPerson $ = Keys.of(ChangeSexPerson.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification personIdentification;
	@NotEmpty
	public ch.ech.ech0044.Sex sex;
}