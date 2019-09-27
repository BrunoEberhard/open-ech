package ch.ech.ech0078;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

// handmade
public class EventReport {
	public static final EventReport $ = Keys.of(EventReport.class);

	public Object id;
	@NotEmpty
	public ReportHeader reportHeader;
	public final ReportInfo info = new ReportInfo();

	// handmade

	public NegativeReport getNegativeReport() {
		return info.negativeReport;
	}

	public void setNegativeReport(NegativeReport negativeReport) {
		info.negativeReport = negativeReport;
	}

	public PositiveReport getPositiveReport() {
		return info.positiveReport;
	}

	public void setPositiveReport(PositiveReport positiveReport) {
		info.positiveReport = positiveReport;
	}
}