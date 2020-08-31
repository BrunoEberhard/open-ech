package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.843334700")
public class EventCorrectPaperLock {
	public static final EventCorrectPaperLock $ = Keys.of(EventCorrectPaperLock.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctPaperLockPerson;
	@NotEmpty
	public ch.openech.model.YesNo paperLock;
	public LocalDate paperLockValidFrom;
	public LocalDate paperLockValidTill;
}