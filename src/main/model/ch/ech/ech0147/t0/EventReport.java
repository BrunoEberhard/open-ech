package ch.ech.ech0147.t0;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventReport {
	public static final EventReport $ = Keys.of(EventReport.class);

	@NotEmpty
	public ReportHeader reportHeader;
	public final Report report = new Report();
}