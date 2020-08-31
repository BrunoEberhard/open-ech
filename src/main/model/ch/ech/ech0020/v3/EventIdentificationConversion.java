package ch.ech.ech0020.v3;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.837334600")
public class EventIdentificationConversion {
	public static final EventIdentificationConversion $ = Keys.of(EventIdentificationConversion.class);

	public Object id;
	public static class IdentificationConversionPerson {
		public static final IdentificationConversionPerson $ = Keys.of(IdentificationConversionPerson.class);

		@Size(13)
		public Long vn;
		public final ch.openech.model.NamedId localPersonIdBefore = new ch.openech.model.NamedId();
		public final ch.openech.model.NamedId localPersonIdAfter = new ch.openech.model.NamedId();
	}
	public List<IdentificationConversionPerson> identificationConversionPerson;
	public LocalDate identificationValidFrom;
}