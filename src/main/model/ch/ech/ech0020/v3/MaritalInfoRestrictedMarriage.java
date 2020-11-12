package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class MaritalInfoRestrictedMarriage {
	public static final MaritalInfoRestrictedMarriage $ = Keys.of(MaritalInfoRestrictedMarriage.class);

	public static class MaritalData {
		public static final MaritalData $ = Keys.of(MaritalData.class);

		@NotEmpty
		public ch.ech.ech0011.MaritalStatus maritalStatus;
		public LocalDate dateOfMaritalStatus;
	}
	public final MaritalData maritalData = new MaritalData();
	public ch.ech.ech0021.MaritalDataAddon maritalDataAddon;
}