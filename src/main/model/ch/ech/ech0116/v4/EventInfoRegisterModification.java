package ch.ech.ech0116.v4;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventInfoRegisterModification {
	public static final EventInfoRegisterModification $ = Keys.of(EventInfoRegisterModification.class);

	public final ReportingRegister reportingRegister = new ReportingRegister();
	@NotEmpty
	public ch.ech.ech0108.Organisation registerOrganisationData;
	@NotEmpty
	public UidInfoAboMessageType uidInfoAboMessageType;
	public byte[] extension;
}