package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventStatusNotification {
	public static final EventStatusNotification $ = Keys.of(EventStatusNotification.class);

	public Object id;
	@NotEmpty
	public EventType eventType;
	@NotEmpty
	public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
	@NotEmpty
	public PlanningPermissionApplicationStatus status;
	public List<String> remark;
	public byte[] extension;
}