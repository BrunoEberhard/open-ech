package ch.ech.ech0046;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.038114")
public class Contact {
	public static final Contact $ = Keys.of(Contact.class);

	public Object id;
	public ch.openech.model.NamedId localID;
	public List<Address> address;
	public List<Email> email;
	public List<Phone> phone;
	public List<Internet> internet;
}