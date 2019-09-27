package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.909")
public class Street {
	public static final Street $ = Keys.of(Street.class);

	@Size(8)
	public Integer ESID;
	public Boolean isOfficialDescription;
	@Size(12)
	public Integer officialStreetNumber;
	public ch.openech.model.NamedId localID;
	public LocalisationKind localisationKind;
	public final LocalisationDescription description = new LocalisationDescription();
	public LocalisationStatus status;
	public NumberingPrinciple numberingPrinciple;
	@Size(7)
	public Integer ESTRID;
	public byte[] streetGeometry;
}