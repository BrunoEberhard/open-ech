package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.832334100")
public class EventDataLock {
	public static final EventDataLock $ = Keys.of(EventDataLock.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification dataLockPerson;
	@NotEmpty
	public ch.ech.ech0021.DataLock dataLock;
	public LocalDate dataLockValidFrom;
	public LocalDate dataLockValidTill;
}