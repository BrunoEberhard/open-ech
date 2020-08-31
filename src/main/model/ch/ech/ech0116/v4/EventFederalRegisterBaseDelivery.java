package ch.ech.ech0116.v4;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.328103700")
public class EventFederalRegisterBaseDelivery {
	public static final EventFederalRegisterBaseDelivery $ = Keys.of(EventFederalRegisterBaseDelivery.class);

	public Object id;
	public final ReportingRegister reportingRegister = new ReportingRegister();
	@NotEmpty
	public List<ch.ech.ech0108.Organisation> organisationRegisterOnly;
	@NotEmpty
	public Integer numberOfOrganisations;
	public byte[] extension;
}