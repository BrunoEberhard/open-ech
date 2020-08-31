package ch.ech.ech0078;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.130083100")
public class ReportInfo {
	public static final ReportInfo $ = Keys.of(ReportInfo.class);

	public NegativeReport negativeReport;
	public PositiveReport positiveReport;
	public PendingReport pendingReport;
	public byte[] data;
}