package ch.ech.ech0011;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class AnyPerson {
	public static final AnyPerson $ = Keys.of(AnyPerson.class);

	public static class Swiss {
		public static final Swiss $ = Keys.of(Swiss.class);

		public List<PlaceOfOrigin> placeOfOrigin;
	}
	public Swiss swiss;
	public static class Foreigner {
		public static final Foreigner $ = Keys.of(Foreigner.class);

		@NotEmpty
		public ch.ech.ech0006.ResidencePermit residencePermit;
		public LocalDate residencePermitTill;
		@Size(100)
		public String nameOnPassport;
	}
	public Foreigner foreigner;
}