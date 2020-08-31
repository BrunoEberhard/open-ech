package ch.ech.ech0129;

import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.339303100")
public class Coordinates {
	public static final Coordinates $ = Keys.of(Coordinates.class);

	public static class LV95 {
		public static final LV95 $ = Keys.of(LV95.class);

		@NotEmpty
		@Size(11)
		public BigDecimal east;
		@NotEmpty
		@Size(11)
		public BigDecimal north;
		@NotEmpty
		public OriginOfCoordinates originOfCoordinates;
	}
	public LV95 LV95;
	public static class LV03 {
		public static final LV03 $ = Keys.of(LV03.class);

		@NotEmpty
		@Size(10)
		public BigDecimal Y;
		@NotEmpty
		@Size(10)
		public BigDecimal X;
		@NotEmpty
		public OriginOfCoordinates originOfCoordinates;
	}
	public LV03 LV03;
}