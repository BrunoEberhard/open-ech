package ch.ech.ech0046;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.060102200")
public class Internet {
	public static final Internet $ = Keys.of(Internet.class);

	public InternetCategory internetCategory;
	@Size(100)
	public String otherInternetCategory;
	@NotEmpty
	@Size(100)
	public String internetAddress;
	public DateRange validity;
}