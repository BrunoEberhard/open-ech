package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.473260100")
public class EventNotice {
	public static final EventNotice $ = Keys.of(EventNotice.class);

	public Object id;
	@NotEmpty
	public EventType eventType;
	@NotEmpty
	public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
	public final DecisionRuling decisionRuling = new DecisionRuling();
	public List<ch.ech.ech0147.t0.Document> document;
	public List<String> remark;
	public byte[] extension;
}