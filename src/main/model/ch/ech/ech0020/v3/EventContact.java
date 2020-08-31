package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.827333700")
public class EventContact {
	public static final EventContact $ = Keys.of(EventContact.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification contactPerson;
	public final ch.ech.ech0011.ContactData contactData = new ch.ech.ech0011.ContactData();
}