package ch.ech.ech0046;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:01.281")
public class ContactRoot {
	public static final ContactRoot $ = Keys.of(ContactRoot.class);

	@NotEmpty
	public Contact contact;
}