package ch.ech.ech0116.v4;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventRegisterRequest {
	public static final EventRegisterRequest $ = Keys.of(EventRegisterRequest.class);

	public final ReportingRegister requestingRegister = new ReportingRegister();
	public OrganisationSearchCriteria searchCriteria;
	public byte[] extension;
}