package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventCorrectContact {
	public static final EventCorrectContact $ = Keys.of(EventCorrectContact.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctContactPerson;
	public ch.ech.ech0011.ContactData contactData;
}