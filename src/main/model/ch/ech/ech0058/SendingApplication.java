package ch.ech.ech0058;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:01.386")
public class SendingApplication {
	public static final SendingApplication $ = Keys.of(SendingApplication.class);

	@NotEmpty
	@Size(30)
	public String manufacturer;
	@NotEmpty
	@Size(30)
	public String product;
	@NotEmpty
	@Size(10)
	public String productVersion;
}