package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventCorrectBirthInfo {
	public static final EventCorrectBirthInfo $ = Keys.of(EventCorrectBirthInfo.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctBirthInfoPerson;
	public final BirthInfo birthInfo = new BirthInfo();
}