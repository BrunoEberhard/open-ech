package ch.ech.ech0116.v4;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventRequestRegisterDeletion {
	public static final EventRequestRegisterDeletion $ = Keys.of(EventRequestRegisterDeletion.class);

	public final ReportingRegister requestingRegister = new ReportingRegister();
	@NotEmpty
	public ch.ech.ech0108.Organisation registerOrganisationData;
	public byte[] extension;
}