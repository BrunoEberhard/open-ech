package ch.ech.ech0108;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class InvolvedPerson {
	public static final InvolvedPerson $ = Keys.of(InvolvedPerson.class);

	@NotEmpty
	@Size(100)
	public String role;
	@Size(13)
	public Long vn;
	@NotEmpty
	@Size(100)
	public String officialName;
	@Size(100)
	public String firstName;
	public ch.openech.model.DatePartiallyKnown dateOfBirth;
	public ch.ech.ech0044.Sex sex;
}