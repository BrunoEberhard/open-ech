package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.841335")
public class EventCorrectBirthInfo {
	public static final EventCorrectBirthInfo $ = Keys.of(EventCorrectBirthInfo.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctBirthInfoPerson;
	public final BirthInfo birthInfo = new BirthInfo();
}