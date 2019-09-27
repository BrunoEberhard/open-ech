package ch.ech.ech0058;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

// handmade
public class EventReport {
	public static final EventReport $ = Keys.of(EventReport.class);

	public Object id;
	@NotEmpty
	public Header header;
	public final Info info = new Info();
	
	//

	public final Report report = new Report();
	
	public class Report {

		public byte[] positiveReport, negativeReport;

	}
	
	public Header getReportHeader() {
		return header;
	}
	
	public void setReportHeader(Header header) {
		this.header = header;
	}
}