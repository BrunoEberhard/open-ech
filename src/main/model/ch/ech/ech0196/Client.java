package ch.ech.ech0196;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.566")
public class Client {
	public static final Client $ = Keys.of(Client.class);

	@NotEmpty
	@Size(40)
	public String clientNumber;
	@NotEmpty
	@Size(11)
	public String tin;
	@NotEmpty
	public ch.ech.ech0010.MrMrs salutation;
	@NotEmpty
	@Size(30)
	public String firstName;
	@NotEmpty
	@Size(30)
	public String lastName;
}