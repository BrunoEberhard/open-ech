package ch.ech.ech0046;

import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:01.203")
public class Address {
	public static final Address $ = Keys.of(Address.class);

	public AddressCategory addressCategory;
	@Size(100)
	public String otherAddressCategory;
	public final ch.ech.ech0010.MailAddress postalAddress = new ch.ech.ech0010.MailAddress();
	public DateRange validity;
}