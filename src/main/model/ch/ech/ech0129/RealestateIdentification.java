package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.753")
public class RealestateIdentification {
	public static final RealestateIdentification $ = Keys.of(RealestateIdentification.class);

	@Size(255) // unknown
	public String EGRID;
	@NotEmpty
	@Size(12)
	public String number;
	@Size(12)
	public String numberSuffix;
	@Size(15)
	public String subDistrict;
	@Size(15)
	public String lot;
}