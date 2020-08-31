package ch.ech.ech0044;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.931439500")
public class PersonIdentificationPartner {
	public static final PersonIdentificationPartner $ = Keys.of(PersonIdentificationPartner.class);

	public Object id;
	@Size(13)
	public Long vn;
	public ch.openech.model.NamedId localPersonId;
	public List<ch.openech.model.NamedId> otherPersonId;
	@NotEmpty
	@Size(100)
	public String officialName;
	@NotEmpty
	@Size(100)
	public String firstName;
	public Sex sex;
	public ch.openech.model.DatePartiallyKnown dateOfBirth;
}