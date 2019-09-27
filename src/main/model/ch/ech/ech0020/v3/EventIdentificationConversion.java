package ch.ech.ech0020.v3;

import java.util.List;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.030")
public class EventIdentificationConversion {
	public static final EventIdentificationConversion $ = Keys.of(EventIdentificationConversion.class);

	public Object id;
	public static class IdentificationConversionPerson {
		public static final IdentificationConversionPerson $ = Keys.of(IdentificationConversionPerson.class);

		@Size(13)
		public Long vn;
		public final ch.openech.xml.NamedId localPersonIdBefore = new ch.openech.xml.NamedId();
		public final ch.openech.xml.NamedId localPersonIdAfter = new ch.openech.xml.NamedId();
	}
	public List<IdentificationConversionPerson> identificationConversionPerson;
	public LocalDate identificationValidFrom;
}