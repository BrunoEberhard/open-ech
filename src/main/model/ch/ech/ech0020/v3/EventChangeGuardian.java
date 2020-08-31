package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.830333200")
public class EventChangeGuardian {
	public static final EventChangeGuardian $ = Keys.of(EventChangeGuardian.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification changeGuardianPerson;
	public final ch.ech.ech0021.GuardianRelationship relationship = new ch.ech.ech0021.GuardianRelationship();
	public LocalDate changeGuardianValidFrom;
}