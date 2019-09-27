package ch.ech.ech0046;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:01.219")
public class Email {
	public static final Email $ = Keys.of(Email.class);

	public EmailCategory emailCategory;
	@Size(100)
	public String otherEmailCategory;
	@NotEmpty
	@Size(100)
	public String emailAddress;
	public DateRange validity;
}