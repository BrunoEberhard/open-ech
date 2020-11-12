package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventChangeCivilDefense {
	public static final EventChangeCivilDefense $ = Keys.of(EventChangeCivilDefense.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification changeCivilDefensePerson;
	public ch.ech.ech0021.CivilDefenseData civilDefenseData;
}