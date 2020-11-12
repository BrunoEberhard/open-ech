package ch.ech.ech0044;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class PersonIdentificationRoot {
	public static final PersonIdentificationRoot $ = Keys.of(PersonIdentificationRoot.class);

	@NotEmpty
	public PersonIdentification personIdentification;
}