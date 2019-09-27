package ch.ech.ech0129;

import java.util.List;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.738")
public class Dwelling {
	public static final Dwelling $ = Keys.of(Dwelling.class);

	public Object id;
	public List<ch.openech.xml.NamedId> localID;
	@Size(12)
	public String administrativeDwellingNo;
	@Size(3)
	public Integer EWID;
	@Size(12)
	public String physicalDwellingNo;
	public ch.openech.xml.DatePartiallyKnown dateOfConstruction;
	public ch.openech.xml.DatePartiallyKnown dateOfDemolition;
	@Size(2)
	public Integer noOfHabitableRooms;
	@Size(4)
	public Integer floor;
	@Size(40)
	public String locationOfDwellingOnFloor;
	public Boolean multipleFloor;
	public UsageLimitation usageLimitation;
	public Kitchen kitchen;
	@Size(4)
	public Integer surfaceAreaOfDwelling;
	public DwellingStatus status;
	public DwellingUsage dwellingUsage;
}