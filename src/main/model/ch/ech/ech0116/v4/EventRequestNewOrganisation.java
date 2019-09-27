package ch.ech.ech0116.v4;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.316")
public class EventRequestNewOrganisation {
	public static final EventRequestNewOrganisation $ = Keys.of(EventRequestNewOrganisation.class);

	public final ReportingRegister requestingRegister = new ReportingRegister();
	@NotEmpty
	public ch.ech.ech0108.Organisation registerOrganisationData;
	public byte[] extension;
}