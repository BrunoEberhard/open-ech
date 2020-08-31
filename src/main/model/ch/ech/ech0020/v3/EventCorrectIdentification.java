package ch.ech.ech0020.v3;

import java.time.LocalDate;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.836335700")
public class EventCorrectIdentification {
	public static final EventCorrectIdentification $ = Keys.of(EventCorrectIdentification.class);

	public final CorrectIdentificationPerson correctIdentificationPerson = new CorrectIdentificationPerson();
	public LocalDate identificationValidFrom;
}