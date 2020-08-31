package ch.ech.ech0201;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.446260600")
public class Person {
	public static final Person $ = Keys.of(Person.class);

	@Size(13)
	public Long vn;
	public final ch.openech.model.NamedId localPersonId = new ch.openech.model.NamedId();
	@NotEmpty
	@Size(100)
	public String officialName;
	@NotEmpty
	@Size(100)
	public String firstName;
	@Size(100)
	public String callName;
	public final ch.openech.model.DatePartiallyKnown dateOfBirth = new ch.openech.model.DatePartiallyKnown();
	public LocalDate dateOfDeath;
	public ch.ech.ech0044.Sex sex;
	@Size(2)
	public String languageOfCorrespondance;
	public ContactData contactData;
}