package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.155")
public class EventCorrectDataLock {
	public static final EventCorrectDataLock $ = Keys.of(EventCorrectDataLock.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctDataLockPerson;
	@NotEmpty
	public ch.ech.ech0021.DataLock dataLock;
	public LocalDate dataLockValidFrom;
	public LocalDate dataLockValidTill;
}