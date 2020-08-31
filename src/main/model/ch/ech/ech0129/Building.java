package ch.ech.ech0129;

import java.util.List;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.358114700")
public class Building {
	public static final Building $ = Keys.of(Building.class);

	public Object id;
	public BuildingIdentification buildingIdentification;
	@Size(9)
	public Integer EGID;
	@Size(12)
	public String officialBuildingNo;
	@Size(40)
	public String name;
	public BuildingDate dateOfConstruction;
	public BuildingDate dateOfRenovation;
	public ch.openech.model.DatePartiallyKnown dateOfDemolition;
	@Size(2)
	public Integer numberOfFloors;
	@Size(3)
	public Integer numberOfSeparateHabitableRooms;
	@Size(5)
	public Integer surfaceAreaOfBuilding;
	@Size(5)
	public Integer subSurfaceAreaOfBuilding;
	@Size(5)
	public Integer surfaceAreaOfBuildingSignaleObject;
	@NotEmpty
	public BuildingCategory buildingCategory;
	@Size(4)
	public Integer buildingClass;
	public BuildingStatus status;
	public Coordinates coordinates;
	public List<ch.openech.model.NamedId> otherID;
	public Boolean civilDefenseShelter;
	@Size(7)
	public Integer quartersCode;
	@Size(6)
	public Integer energyRelevantSurface;
	public Boolean thermoTechnicalDeviceYesNo;
	public BuildingVolume volume;
	public List<ThermotechnicalDevice> thermotechnicalDevice;
	public List<BuildingEntrance> buildingEntrance;
	public PersonOrOrganisation realEstateDeputy;
	public List<NamedMetaData> namedMetaData;
}