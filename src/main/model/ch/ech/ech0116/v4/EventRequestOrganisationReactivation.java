package ch.ech.ech0116.v4;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventRequestOrganisationReactivation {
	public static final EventRequestOrganisationReactivation $ = Keys.of(EventRequestOrganisationReactivation.class);

	public final ReportingRegister requestingRegister = new ReportingRegister();
	public final ch.openech.model.UidStructure uid = new ch.openech.model.UidStructure();
	public byte[] extension;
}