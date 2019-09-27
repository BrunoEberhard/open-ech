package ch.ech.ech0129;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.503")
public class BuildingIdentification {
	public static final BuildingIdentification $ = Keys.of(BuildingIdentification.class);

	public Object id;
	@Size(9)
	public Integer EGID;
	@Size(60)
	public String street;
	@Size(12)
	public String houseNumber;
	@Size(4)
	public Integer zipCode;
	@Size(40)
	public String nameOfBuilding;
	@Size(14)
	public String EGRID;
	@Size(255) // unknown
	public String cadasterAreaNumber;
	@Size(12)
	public String number;
	public RealestateType realestateType;
	@Size(12)
	public String officialBuildingNo;
	public List<ch.openech.model.NamedId> localID;
	@NotEmpty
	@Size(4)
	public Integer municipality;
}