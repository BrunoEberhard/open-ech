package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.811")
public class ChangeSexPerson {
	public static final ChangeSexPerson $ = Keys.of(ChangeSexPerson.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification personIdentification;
	@NotEmpty
	public ch.ech.ech0044.Sex sex;
}