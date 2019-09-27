package ch.ech.ech0078;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:01.495")
public class PersonObject {
	public static final PersonObject $ = Keys.of(PersonObject.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification personIdentification;
}