package ch.ech.ech0011;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.452")
public class SwissMunicipalityWithoutBFS {
	public static final SwissMunicipalityWithoutBFS $ = Keys.of(SwissMunicipalityWithoutBFS.class);

	@Size(4)
	public Integer municipalityId;
	@NotEmpty
	@Size(40)
	public String municipalityName;
	public ch.ech.ech0071.CantonAbbreviation cantonAbbreviation;
}