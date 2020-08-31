package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.472260200")
public class EventNoticeInvolvedParty {
	public static final EventNoticeInvolvedParty $ = Keys.of(EventNoticeInvolvedParty.class);

	public Object id;
	@NotEmpty
	public EventType eventType;
	@NotEmpty
	public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
	public final DecisionAuthority decisionAuthority = new DecisionAuthority();
	public final EntryOffice entryOffice = new EntryOffice();
	public List<SpecialistDepartment> specialistDepartment;
	public List<String> remark;
	public List<ch.ech.ech0147.t0.Document> document;
	public byte[] extension;
}