package ch.ech.ech0173;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.423260500")
public class EventPersonData {
	public static final EventPersonData $ = Keys.of(EventPersonData.class);

	public Object id;
	public final Requester requester = new Requester();
	@Size(700)
	public String justification;
	public RequestetData requestedData;
	public ch.ech.ech0007.SwissMunicipality municipality;
	public static class Person {
		public static final Person $ = Keys.of(Person.class);

		public static class PersonIdentification {
			public static final PersonIdentification $ = Keys.of(PersonIdentification.class);

			@Size(13)
			public Long vn;
			public final ch.openech.model.NamedId localPersonId = new ch.openech.model.NamedId();
			public List<ch.openech.model.NamedId> otherPersonId;
			public List<ch.openech.model.NamedId> euPersonId;
			@NotEmpty
			@Size(100)
			public String officialName;
			@NotEmpty
			@Size(100)
			public String firstName;
			@Size(100)
			public String originalName;
			@NotEmpty
			public ch.ech.ech0044.Sex sex;
			public final ch.openech.model.DatePartiallyKnown dateOfBirth = new ch.openech.model.DatePartiallyKnown();
			@Size(100)
			public String callName;
			public ch.ech.ech0011.ForeignerName nameOnForeignPassport;
			public ch.ech.ech0011.ForeignerName declaredForeignName;
		}
		@NotEmpty
		public PersonIdentification personIdentification;
		public ch.ech.ech0010.AddressInformation address;
	}
	public List<Person> person;
	public static class Organisation {
		public static final Organisation $ = Keys.of(Organisation.class);

		@NotEmpty
		public ch.ech.ech0097.OrganisationIdentification organisationIdentification;
		public ch.ech.ech0010.AddressInformation address;
	}
	public List<Organisation> organisation;
	public LocalDate dateValidFrom;
	public byte[] extension;
}