package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.952")
public class EventPaperLock {
	public static final EventPaperLock $ = Keys.of(EventPaperLock.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification paperLockPerson;
	@NotEmpty
	public ch.openech.xml.YesNo paperLock;
	public LocalDate paperLockValidFrom;
	public LocalDate paperLockValidTill;
}