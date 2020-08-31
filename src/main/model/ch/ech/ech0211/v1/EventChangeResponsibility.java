package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.473260100")
public class EventChangeResponsibility {
	public static final EventChangeResponsibility $ = Keys.of(EventChangeResponsibility.class);

	public Object id;
	@NotEmpty
	public EventType eventType;
	@NotEmpty
	public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
	public final EntryOffice entryOffice = new EntryOffice();
	public final DecisionAuthority responsibleDecisionAuthority = new DecisionAuthority();
	public List<String> remark;
	public byte[] extension;
}