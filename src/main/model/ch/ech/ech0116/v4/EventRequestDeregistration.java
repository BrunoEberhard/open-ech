package ch.ech.ech0116.v4;

import java.util.List;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventRequestDeregistration {
	public static final EventRequestDeregistration $ = Keys.of(EventRequestDeregistration.class);

	public Object id;
	public final ReportingRegister requestingRegister = new ReportingRegister();
	public List<ch.openech.model.UidStructure> uid;
	public byte[] extension;
}