package ch.ech.ech0071;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class Nomenclature {
	public static final Nomenclature $ = Keys.of(Nomenclature.class);

	@NotEmpty
	public LocalDate validFrom;
	public static class Cantons {
		public static final Cantons $ = Keys.of(Cantons.class);

		public List<Canton> canton;
	}
	@NotEmpty
	public Cantons cantons;
	public static class Districts {
		public static final Districts $ = Keys.of(Districts.class);

		public List<District> district;
	}
	@NotEmpty
	public Districts districts;
	public static class Municipalities {
		public static final Municipalities $ = Keys.of(Municipalities.class);

		public List<Municipality> municipality;
	}
	@NotEmpty
	public Municipalities municipalities;
}