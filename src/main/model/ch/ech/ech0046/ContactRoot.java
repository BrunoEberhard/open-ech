package ch.ech.ech0046;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.090117")
public class ContactRoot {
	public static final ContactRoot $ = Keys.of(ContactRoot.class);

	@NotEmpty
	public Contact contact;
}