package ch.ech.ech0044;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.967331500")
public class PersonIdentificationRoot {
	public static final PersonIdentificationRoot $ = Keys.of(PersonIdentificationRoot.class);

	@NotEmpty
	public PersonIdentification personIdentification;
}