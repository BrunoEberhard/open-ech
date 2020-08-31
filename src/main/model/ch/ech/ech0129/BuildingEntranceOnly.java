package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.360083")
public class BuildingEntranceOnly {
	public static final BuildingEntranceOnly $ = Keys.of(BuildingEntranceOnly.class);

	@Size(9)
	public Integer EGAID;
	@Size(2)
	public Integer EDID;
	@Size(12)
	public String buildingEntranceNo;
	public EntranceStatus status;
	public Coordinates coordinates;
	public final ch.openech.model.NamedId localID = new ch.openech.model.NamedId();
	public Boolean isMainAddress;
	public Boolean isOfficial;
}