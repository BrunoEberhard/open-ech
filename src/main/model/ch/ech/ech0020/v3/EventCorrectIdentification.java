package ch.ech.ech0020.v3;

import java.time.LocalDate;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.030")
public class EventCorrectIdentification {
	public static final EventCorrectIdentification $ = Keys.of(EventCorrectIdentification.class);

	public final CorrectIdentificationPerson correctIdentificationPerson = new CorrectIdentificationPerson();
	public LocalDate identificationValidFrom;
}