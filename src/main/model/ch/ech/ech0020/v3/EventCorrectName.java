package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.838335900")
public class EventCorrectName {
	public static final EventCorrectName $ = Keys.of(EventCorrectName.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctNamePerson;
	public final NameInfo nameInfo = new NameInfo();
}