package ch.ech.ech0078;

import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:01.542")
public class ReportInfo {
	public static final ReportInfo $ = Keys.of(ReportInfo.class);

	public NegativeReport negativeReport;
	public PositiveReport positiveReport;
	public PendingReport pendingReport;
	public byte[] data;
}