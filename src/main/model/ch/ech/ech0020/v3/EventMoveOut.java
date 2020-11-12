package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventMoveOut {
	public static final EventMoveOut $ = Keys.of(EventMoveOut.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification moveOutPerson;
	public final ReportingMunicipality moveOutReportingDestination = new ReportingMunicipality();
}