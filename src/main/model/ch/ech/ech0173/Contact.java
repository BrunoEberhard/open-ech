package ch.ech.ech0173;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.425")
public class Contact {
	public static final Contact $ = Keys.of(Contact.class);

	public Object id;
	public ch.openech.model.NamedId localID;
	public static class Address {
		public static final Address $ = Keys.of(Address.class);


		public enum AddressCategory { _1, _2;		}
		public AddressCategory addressCategory;
		@Size(100)
		public String otherAddressCategory;
		public ch.ech.ech0010.MailAddress postalAddress;
		public static class Validity {
			public static final Validity $ = Keys.of(Validity.class);

			public LocalDate dateFrom;
			public LocalDate dateTo;
		}
		public Validity validity;
	}
	public List<Address> address;
	public static class Email {
		public static final Email $ = Keys.of(Email.class);


		public enum EmailCategory { _1, _2;		}
		public EmailCategory emailCategory;
		@Size(100)
		public String otherEmailCategory;
		@NotEmpty
		@Size(100)
		public String emailAddress;
		public static class Validity_ {
			public static final Validity_ $ = Keys.of(Validity_.class);

			public LocalDate dateFrom;
			public LocalDate dateTo;
		}
		public Validity_ validity;
	}
	public List<Email> email;
	public static class Phone {
		public static final Phone $ = Keys.of(Phone.class);


		public enum PhoneCategory { _1, _2, _3, _4, _5, _6, _7, _8, _9, _10;		}
		public PhoneCategory phoneCategory;
		@Size(100)
		public String otherPhoneCategory;
		@NotEmpty
		@Size(20)
		public String phoneNumber;
		public static class Validity__ {
			public static final Validity__ $ = Keys.of(Validity__.class);

			public LocalDate dateFrom;
			public LocalDate dateTo;
		}
		public Validity__ validity;
	}
	public List<Phone> phone;
	public static class Internet {
		public static final Internet $ = Keys.of(Internet.class);


		public enum InternetCategory { _1, _2;		}
		public InternetCategory internetCategory;
		@Size(100)
		public String otherInternetCategory;
		@NotEmpty
		@Size(100)
		public String internetAddress;
		public static class Validity___ {
			public static final Validity___ $ = Keys.of(Validity___.class);

			public LocalDate dateFrom;
			public LocalDate dateTo;
		}
		public Validity___ validity;
	}
	public List<Internet> internet;
}