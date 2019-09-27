package ch.ech.ech0116.v4;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.347")
public class EventRequestOrganisationReactivation {
	public static final EventRequestOrganisationReactivation $ = Keys.of(EventRequestOrganisationReactivation.class);

	public final ReportingRegister requestingRegister = new ReportingRegister();
	public final ch.openech.xml.UidStructure uid = new ch.openech.xml.UidStructure();
	public byte[] extension;
}