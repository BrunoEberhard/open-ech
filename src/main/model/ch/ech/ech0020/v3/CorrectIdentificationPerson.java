package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class CorrectIdentificationPerson {
	public static final CorrectIdentificationPerson $ = Keys.of(CorrectIdentificationPerson.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification personIdentificationBefore;
	@NotEmpty
	public PersonIdOnly personIdentificationAfter;
}