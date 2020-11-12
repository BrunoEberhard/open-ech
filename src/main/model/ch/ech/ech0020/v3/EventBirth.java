package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventBirth {
	public static final EventBirth $ = Keys.of(EventBirth.class);

	@NotEmpty
	public BirthPerson birthPerson;
	public ch.ech.ech0011.MainResidence hasMainResidence;
	public ch.ech.ech0011.SecondaryResidence hasSecondaryResidence;
	public ch.ech.ech0011.OtherResidence hasOtherResidence;
}