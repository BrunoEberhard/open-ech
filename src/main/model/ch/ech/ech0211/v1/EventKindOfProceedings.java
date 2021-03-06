package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventKindOfProceedings {
	public static final EventKindOfProceedings $ = Keys.of(EventKindOfProceedings.class);

	public Object id;
	@NotEmpty
	public EventType eventType;
	@NotEmpty
	public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
	@NotEmpty
	public List<ch.ech.ech0147.t0.Document> document;
	public List<String> remark;
	public byte[] extension;
}