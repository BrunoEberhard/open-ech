package ch.ech.ech0044;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.891")
public class PersonIdentificationPartner {
	public static final PersonIdentificationPartner $ = Keys.of(PersonIdentificationPartner.class);

	public Object id;
	@Size(13)
	public Long vn;
	public ch.openech.xml.NamedId localPersonId;
	public List<ch.openech.xml.NamedId> otherPersonId;
	@NotEmpty
	@Size(100)
	public String officialName;
	@NotEmpty
	@Size(100)
	public String firstName;
	public Sex sex;
	public ch.openech.xml.DatePartiallyKnown dateOfBirth;
}