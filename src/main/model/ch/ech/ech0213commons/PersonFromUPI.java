package ch.ech.ech0213commons;

import java.time.LocalDateTime;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class PersonFromUPI {
	public static final PersonFromUPI $ = Keys.of(PersonFromUPI.class);

	public LocalDateTime recordTimestamp;
	@NotEmpty
	@Size(100)
	public String firstName;
	@NotEmpty
	@Size(100)
	public String officialName;
	@Size(100)
	public String originalName;
	public ch.ech.ech0011.ForeignerName nameOnForeignPassport;
	@NotEmpty
	public ch.ech.ech0044.Sex sex;
	public final ch.openech.model.DatePartiallyKnown dateOfBirth = new ch.openech.model.DatePartiallyKnown();
	public final ch.ech.ech0011.GeneralPlace placeOfBirth = new ch.ech.ech0011.GeneralPlace();
	public ch.ech.ech0021.NameOfParent mothersName;
	public ch.ech.ech0021.NameOfParent fathersName;
	public final ch.ech.ech0011.NationalityData nationalityData = new ch.ech.ech0011.NationalityData();
	public LocalDate dateOfDeath;
}