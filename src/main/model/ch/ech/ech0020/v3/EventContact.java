package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.842")
public class EventContact {
	public static final EventContact $ = Keys.of(EventContact.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification contactPerson;
	public final ch.ech.ech0011.ContactData contactData = new ch.ech.ech0011.ContactData();
}