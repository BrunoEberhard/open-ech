package ch.ech.ech0020.v3;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventMove {
	public static final EventMove $ = Keys.of(EventMove.class);

	@NotEmpty
	public ch.ech.ech0044.PersonIdentification movePerson;
	public final ReportingMunicipality moveReportingMunicipality = new ReportingMunicipality();
}