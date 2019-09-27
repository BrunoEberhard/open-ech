package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.972")
public class PlaceName {
	public static final PlaceName $ = Keys.of(PlaceName.class);

	@NotEmpty
	public PlaceNameType placeNameType;
	@NotEmpty
	@Size(255) // unknown
	public String localGeographicalName;
}