package ch.openech.model.estate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;

import ch.openech.model.EchFormats;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.NamedId;
import ch.openech.model.common.NamedMetaData;
import ch.openech.model.types.EchCode;

@Sizes(EchFormats.class)
public class Building {

	public BuildingIdentification buildingIdentification;
	public String EGID;
	public String officialBuildingNo;
	@Size(EchFormats.nameOfBuilding)
	public String name;
	public BuildingDate dateOfConstruction, dateOfRenovation;
	public DatePartiallyKnown dateOfDemolition;
	public Integer numberOfFloor, numberOfSeparateHabitableRooms;
	public Integer surfaceAreaOfBuilding, subSurfaceAreaOfBuilding;
	@Size(EchFormats.surfaceAreaOfBuildingSingleObject) // typo ech
	public Integer surfaceAreaOfBuildingSignaleObject;
	public BuildingCategory buildingCategory;
	// public BuildingClass buildingClass; // TODO EUROSTAT
	public BuildingStatus status;
	public List<NamedId> otherID;
	public Boolean civilDefenseShelter;
	public Coordinates coordinates;
	public Integer quartersCode;
	public Integer energyRelevantSurface;
	public Boolean thermoTechnicalDeviceYesNo;
	public final BuildingVolume volume = new BuildingVolume();
	public List<NamedMetaData> namedMetaData = new ArrayList<NamedMetaData>();
	public ThermotechnicalDevice thermotechnicalDevice1;
	public ThermotechnicalDevice thermotechnicalDevice2;
	public List<BuildingEntrance> buildingEntrance;
	
	/*
	<xs:element name="buildingIdentification" type="eCH-0129:buildingIdentificationType" minOccurs="0"/>
	<xs:element name="EGID" type="eCH-0129:EGIDType" minOccurs="0"/>
	<xs:element name="officialBuildingNo" type="eCH-0129:officialBuildingNoType" minOccurs="0"/>
	<xs:element name="name" type="eCH-0129:nameOfBuildingType" minOccurs="0"/>
	<xs:element name="dateOfConstruction" type="eCH-0129:buildingDateType" minOccurs="0"/>
	<xs:element name="dateOfRenovation" type="eCH-0129:buildingDateType" minOccurs="0"/>
	<xs:element name="dateOfDemolition" type="eCH-0129:datePartiallyKnownType" minOccurs="0"/>
	<xs:element name="numberOfFloors" type="eCH-0129:numberOfFloorsType" minOccurs="0"/>
	<xs:element name="numberOfSeparateHabitableRooms" type="eCH-0129:numberOfSeparateHabitableRoomsType" minOccurs="0"/>
	<xs:element name="surfaceAreaOfBuilding" type="eCH-0129:surfaceAreaOfBuildingType" minOccurs="0"/>
	<xs:element name="subSurfaceAreaOfBuilding" type="eCH-0129:surfaceAreaOfBuildingType" minOccurs="0"/>
	<xs:element name="surfaceAreaOfBuildingSignaleObject" type="eCH-0129:surfaceAreaOfBuildingSingleObjectType" minOccurs="0"/>
	<xs:element name="buildingCategory" type="eCH-0129:buildingCategoryType"/>
	<xs:element name="buildingClass" type="eCH-0129:buildingClassType" minOccurs="0"/>
	<xs:element name="status" type="eCH-0129:buildingStatusType" minOccurs="0"/>
	<xs:element name="coordinates" type="eCH-0129:coordinatesType" minOccurs="0"/>
	<xs:element name="otherID" type="eCH-0129:namedIdType" minOccurs="0" maxOccurs="unbounded"/>
	<xs:element name="civilDefenseShelter" type="xs:boolean" minOccurs="0"/>
	<xs:element name="quartersCode" type="eCH-0129:quartersCodeType" minOccurs="0"/>
	<xs:element name="energyRelevantSurface" type="eCH-0129:energyRelevantSurfaceType" minOccurs="0"/>
	<xs:element name="thermoTechnicalDeviceYesNo" type="xs:boolean" minOccurs="0"/>
	<xs:element name="volume" type="eCH-0129:buildingVolumeType" minOccurs="0"/>
	<xs:element name="thermotechnicalDevice" type="eCH-0129:thermotechnicalDeviceType" minOccurs="0" maxOccurs="2"/>
	<xs:element name="buildingEntrance" type="eCH-0129:buildingEntranceType" minOccurs="0" maxOccurs="unbounded"/>
	
	<xs:element name="realEstateDeputy" type="eCH-0129:personIdentificationType" minOccurs="0"/>
	<xs:element name="namedMetaData" type="eCH-0129:namedMetaDataType" minOccurs="0" maxOccurs="unbounded"/>
	*/
	
	public static class BuildingVolume {
		@Size(7)
		public Integer volume;
		public BuildingVolumeInformationSource informationSource;
		public BuildingVolumeNorm norm;
		
	}
	
	public static enum BuildingVolumeInformationSource implements EchCode {
		_869, _858, _853, _852, _857, _851, _870, _878, _859;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}

	public static enum BuildingVolumeNorm implements EchCode {
		_961, _962, _969;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}

	public static class ThermotechnicalDevice {
		@NotEmpty
		public ThermotechnicalHeatGenerator heatGenerator;
		public ThermotechnicalDeviceHeatingType heatingType;
		public ThermotechnicalDeviceInformationSource informationSource;
		public LocalDate revisionDate;
		@NotEmpty
		public Boolean heatForHeating, heatForWarmwater;
		public ThermotechnicalDeviceEnergySource energySource;
		
	}

	public static enum ThermotechnicalHeatGenerator implements EchCode {
		_7401, _7410, _7420, _7430, _7431, _7440, _7450, _7451, _7460, _7499;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
	
	public static enum ThermotechnicalDeviceHeatingType implements EchCode {
		_7701, _7702, _7703;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}

	public static enum ThermotechnicalDeviceInformationSource implements EchCode {
		_869, _858, _871, _855, _864, _853, _852, _857, _860, _859;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}

	public static enum ThermotechnicalDeviceEnergySource implements EchCode {
		_7501, _7511, _7512, _7513, _7520, _7530, _7540, _7541, _7542, _7543, _7550, _7560, _7570, _7580, _7598, _7599;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
	
	public static class BuildingEntrance {
		public String EGAID, EDID, buildingEntranceNo;
		public EntranceStatus status;
		public Coordinates coordinates;
		public NamedId localId;
		public Boolean isMainAddress, isOfficial;
		@NotEmpty
		public Locality locality;
		@NotEmpty
		public Street street;
	}
	
	public static enum EntranceStatus implements EchCode {
		_2001, _2002, _2003, _2004, _2005, _2007, _2008, _2009;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
}
