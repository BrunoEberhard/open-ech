package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.828333100")
public class EventMoveOut {
	public static final EventMoveOut $ = Keys.of(EventMoveOut.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification moveOutPerson;
	public final ReportingMunicipality moveOutReportingDestination = new ReportingMunicipality();
}