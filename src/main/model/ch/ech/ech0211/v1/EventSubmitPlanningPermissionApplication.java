package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.470260")
public class EventSubmitPlanningPermissionApplication {
	public static final EventSubmitPlanningPermissionApplication $ = Keys.of(EventSubmitPlanningPermissionApplication.class);

	public Object id;
	@NotEmpty
	public EventType eventType;
	@NotEmpty
	public PlanningPermissionApplication planningPermissionApplication;
	public List<RelationshipToPerson> relationshipToPerson;
	public byte[] extension;
}