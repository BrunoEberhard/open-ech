package ch.ech.ech0135;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class PlaceOfOrigin {
	public static final PlaceOfOrigin $ = Keys.of(PlaceOfOrigin.class);

	public LocalDate validFrom;
	public LocalDate validTo;
	@NotEmpty
	public Integer placeOfOriginId;
	@Size(5)
	public Integer historyMunicipalityId;
	@NotEmpty
	@Size(255) // unknown
	public String placeOfOriginName;
	@NotEmpty
	public ch.ech.ech0071.CantonAbbreviation cantonAbbreviation;
	public Integer successorId;
}