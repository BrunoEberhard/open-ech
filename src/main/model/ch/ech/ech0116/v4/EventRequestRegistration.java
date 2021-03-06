package ch.ech.ech0116.v4;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventRequestRegistration {
	public static final EventRequestRegistration $ = Keys.of(EventRequestRegistration.class);

	public Object id;
	public final ReportingRegister requestingRegister = new ReportingRegister();
	public List<ch.openech.model.UidStructure> uid;
	@NotEmpty
	public InfoAboRegistrationType infoAboRegistrationType;
	public byte[] extension;
}