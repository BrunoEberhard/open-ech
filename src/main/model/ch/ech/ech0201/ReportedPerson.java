package ch.ech.ech0201;

import java.time.LocalDate;
import java.util.List;

import org.minimalj.model.Keys;

import ch.ech.ech0020.v3.ReportingMunicipality;

//handmade
public class ReportedPerson {
	public static final ReportedPerson $ = Keys.of(ReportedPerson.class);

	public final Person person = new Person();
	public static class HasMainResidence {

		public List<ch.ech.ech0007.SwissMunicipality> secondaryResidence;
	}
	public HasMainResidence hasMainResidence;
	public ReportingMunicipality hasOtherResidence;
	public LocalDate reportedPersonValidFrom;
}