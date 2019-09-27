package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.644")
public class BuildingEntrance {
	public static final BuildingEntrance $ = Keys.of(BuildingEntrance.class);

	@Size(9)
	public Integer EGAID;
	@Size(2)
	public Integer EDID;
	@Size(12)
	public String buildingEntranceNo;
	public EntranceStatus status;
	public Coordinates coordinates;
	public final ch.openech.xml.NamedId localID = new ch.openech.xml.NamedId();
	public Boolean isMainAddress;
	public Boolean isOfficial;
	public final Locality locality = new Locality();
	public final Street street = new Street();
}