package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.983")
public class EventCorrectReporting {
	public static final EventCorrectReporting $ = Keys.of(EventCorrectReporting.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification correctReportingPerson;
	public ch.ech.ech0011.MainResidence hasMainResidence;
	public ch.ech.ech0011.SecondaryResidence hasSecondaryResidence;
	public ch.ech.ech0011.OtherResidence hasOtherResidence;
	public LocalDate reportingValidFrom;
}