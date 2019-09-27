package ch.ech.ech0173;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.441")
public class EventAddressVerification {
	public static final EventAddressVerification $ = Keys.of(EventAddressVerification.class);

	public Object id;
	public final Requester requester = new Requester();
	@Size(700)
	public String justification;
	@NotEmpty
	public Boolean includeResidenceTypeYesNo;
	public ch.ech.ech0007.SwissMunicipality municipality;
	public static class Person {
		public static final Person $ = Keys.of(Person.class);

		public static class PersonIdentification {
			public static final PersonIdentification $ = Keys.of(PersonIdentification.class);

			@Size(13)
			public Long vn;
			public final ch.openech.xml.NamedId localPersonId = new ch.openech.xml.NamedId();
			public List<ch.openech.xml.NamedId> otherPersonId;
			public List<ch.openech.xml.NamedId> euPersonId;
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
			public final ch.openech.xml.DatePartiallyKnown dateOfBirth = new ch.openech.xml.DatePartiallyKnown();
			@Size(100)
			public String callName;
			public ch.ech.ech0011.ForeignerName nameOnForeignPassport;
			public ch.ech.ech0011.ForeignerName declaredForeignName;
		}
		@NotEmpty
		public PersonIdentification personIdentification;
		public final ch.ech.ech0010.AddressInformation address = new ch.ech.ech0010.AddressInformation();
	}
	public List<Person> person;
	public static class Organisation {
		public static final Organisation $ = Keys.of(Organisation.class);

		@NotEmpty
		public ch.ech.ech0097.OrganisationIdentification organisationIdentification;
		public final ch.ech.ech0010.AddressInformation address = new ch.ech.ech0010.AddressInformation();
	}
	public List<Organisation> organisation;
	public LocalDate dateValidFrom;
	public byte[] extension;
}