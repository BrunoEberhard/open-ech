package ch.ech.ech0011;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.767318200")
public class PlaceOfOrigin {
	public static final PlaceOfOrigin $ = Keys.of(PlaceOfOrigin.class);

	@NotEmpty
	@Size(50)
	public String originName;
	@NotEmpty
	public ch.ech.ech0071.CantonAbbreviation canton;
	public Integer placeOfOriginId;
	@Size(5)
	public Integer historyMunicipalityId;
}