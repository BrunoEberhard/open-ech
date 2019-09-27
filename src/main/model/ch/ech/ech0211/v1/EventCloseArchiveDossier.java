package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.088")
public class EventCloseArchiveDossier {
	public static final EventCloseArchiveDossier $ = Keys.of(EventCloseArchiveDossier.class);

	public Object id;
	@NotEmpty
	public EventType eventType;
	@NotEmpty
	public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
	public List<String> remark;
	public byte[] extension;
}