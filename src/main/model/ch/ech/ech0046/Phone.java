package ch.ech.ech0046;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.054114500")
public class Phone {
	public static final Phone $ = Keys.of(Phone.class);

	public PhoneCategory phoneCategory;
	@Size(100)
	public String otherPhoneCategory;
	@NotEmpty
	@Size(20)
	public String phoneNumber;
	public DateRange validity;
}