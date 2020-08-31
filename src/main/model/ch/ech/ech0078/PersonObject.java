package ch.ech.ech0078;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.128083100")
public class PersonObject {
	public static final PersonObject $ = Keys.of(PersonObject.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification personIdentification;
}