package ch.ech.ech0211.v1;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventAccompanyingReport {
	public static final EventAccompanyingReport $ = Keys.of(EventAccompanyingReport.class);

	public Object id;
	@NotEmpty
	public EventType eventType;
	public Judgement judgement;
	@NotEmpty
	public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
	public ch.ech.ech0147.t2.Directive directive;
	@NotEmpty
	public List<ch.ech.ech0147.t0.Document> document;
	public List<String> remark;
	public List<String> ancillaryClauses;
	public byte[] extension;
}