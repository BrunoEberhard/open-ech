package ch.ech.ech0116.v4;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.330083200")
public class EventRequestRegisterModification {
	public static final EventRequestRegisterModification $ = Keys.of(EventRequestRegisterModification.class);

	public final ReportingRegister requestingRegister = new ReportingRegister();
	@NotEmpty
	public ch.ech.ech0108.Organisation registerOrganisationData;
	public byte[] extension;
}