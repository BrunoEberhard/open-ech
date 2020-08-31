package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.470260")
public class EventChangeContact {
	public static final EventChangeContact $ = Keys.of(EventChangeContact.class);

	public Object id;
	@NotEmpty
	public EventType eventType;
	@NotEmpty
	public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
	public ch.ech.ech0147.t2.Directive directive;
	public List<RelationshipToPerson> relationshipToPerson;
	public List<String> remark;
	public byte[] extension;
}