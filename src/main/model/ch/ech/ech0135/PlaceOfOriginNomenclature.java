package ch.ech.ech0135;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.097")
public class PlaceOfOriginNomenclature {
	public static final PlaceOfOriginNomenclature $ = Keys.of(PlaceOfOriginNomenclature.class);

	public static class PlaceOfOrigins {
		public static final PlaceOfOrigins $ = Keys.of(PlaceOfOrigins.class);

		public List<PlaceOfOrigin> placeOfOrigin;
	}
	@NotEmpty
	public PlaceOfOrigins placeOfOrigins;
}