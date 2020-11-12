package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventRequest {
	public static final EventRequest $ = Keys.of(EventRequest.class);

	public Object id;
	@NotEmpty
	public EventType eventType;
	@NotEmpty
	public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
	public ch.ech.ech0147.t2.Directive directive;
	public List<ch.ech.ech0147.t0.Document> document;
	public List<String> remark;
	public byte[] extension;
}