package ch.ech.ech0213commons;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class PersonToUPI {
	public static final PersonToUPI $ = Keys.of(PersonToUPI.class);

	@NotEmpty
	@Size(100)
	public String firstName;
	@NotEmpty
	@Size(100)
	public String officialName;
	@Size(100)
	public String originalName;
	public ch.ech.ech0044.Sex sex;
	public final ch.openech.model.DatePartiallyKnown dateOfBirth = new ch.openech.model.DatePartiallyKnown();
	public ch.ech.ech0011.GeneralPlace placeOfBirth;
	public ch.ech.ech0021.NameOfParent mothersName;
	public ch.ech.ech0021.NameOfParent fathersName;
	public ch.ech.ech0011.NationalityData nationalityData;
}