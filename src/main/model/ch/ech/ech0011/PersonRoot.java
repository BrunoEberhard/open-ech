package ch.ech.ech0011;

import org.minimalj.model.Keys;

// handmade
public class PersonRoot {
	public static final PersonRoot $ = Keys.of(PersonRoot.class);

	public Object id;
	public final ReportedPerson reportedPerson = new ReportedPerson();
	
	// typo in early ech 0011
	public ReportedPerson getReportetPerson() {
		return reportedPerson;
	}
	
}