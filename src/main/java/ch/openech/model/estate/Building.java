package ch.openech.model.estate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.View;
import org.minimalj.model.annotation.Enabled;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;
import org.minimalj.repository.sql.EmptyObjects;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.NamedId;
import ch.openech.model.common.NamedMetaData;
import ch.openech.model.types.EchCode;

@Sizes(EchFormats.class)
public class Building {

	public static final Building $ = Keys.of(Building.class);

	public Object id;

	public transient BuildingIdentificationType identificationType = BuildingIdentificationType.EGID;
	public static enum BuildingIdentificationType {
		EGID, ADDRESS, EGRID, CADASTER;
	}
	
	public boolean hasADDRESSIdentification() {
		return identificationType == BuildingIdentificationType.ADDRESS;
	}

	public boolean hasEGRIDIdentification() {
		return identificationType == BuildingIdentificationType.EGRID;
	}

	public boolean hasCADASTERIdentification() {
		return identificationType == BuildingIdentificationType.CADASTER;
	}
	
	public BuildingIdentificationType identificationType() {
		if (!EmptyObjects.isEmpty(EGID)) {
			return BuildingIdentificationType.EGID;
		} else if (!EmptyObjects.isEmpty(street)) {
			return BuildingIdentificationType.ADDRESS;
		} else if (!EmptyObjects.isEmpty(EGRID)) {
			return BuildingIdentificationType.EGRID;
		} else if (!EmptyObjects.isEmpty(cadasterAreaNumber)) {
			return BuildingIdentificationType.CADASTER;
		} else {
			return null;
		}
	}

	// einer von a, b, c
	// a)
	public Integer EGID; // ist in xsd V4 auf identification und objekt (daher kein @Enabled)
	
	// b)
	@Enabled("hasADDRESSIdentification")
	public String street, houseNumber, nameOfBuilding;
	@Enabled("hasADDRESSIdentification")
	public Integer zipCode;
	
	// c)
	@Enabled("hasEGRIDIdentification")
	public String EGRID;
	@Enabled("hasCADASTERIdentification")
	@Size(255) // missing size in 0129
	public String cadasterAreaNumber;
	@Enabled("hasCADASTERIdentification")
	@Size(12)
	public String number;
	@Enabled("hasCADASTERIdentification")
	public RealestateType realestateType;
	@Size(12)
	public String officialBuildingNo; // ist in xsd V4 auf identification und objekt (daher kein @Enabled)
	
	public List<NamedId> localID = new ArrayList<>();
	@NotEmpty
	public MunicipalityIdentification municipality;
	
	//
	
	@Size(EchFormats.nameOfBuilding)
	public String name;
	public BuildingDate dateOfConstruction, dateOfRenovation;
	public DatePartiallyKnown dateOfDemolition;
	public Integer numberOfFloors, numberOfSeparateHabitableRooms;
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
	public final List<ThermotechnicalDevice> thermotechnicalDevice = new ArrayList<>();

	// Die Eingänge werden direkt auf dem Gebäude gehalten
	public final List<BuildingEntrance> buildingEntrance = new ArrayList<>();
	
	// nicht bei eCH / View
	public List<Dwelling> dwelling = new ArrayList<>();
	
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
	

	public String getStreetAndHouseNumber() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "streetAndHouseNumber");
		
		if (!StringUtils.isEmpty(street)) {
			return street + " " + houseNumber;
		} else {
			return null;
		}
	}
	
	
	public enum BuildingStatus implements EchCode {

		_1001, _1002, _1003, _1004, _1005, _1007, _1008;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}

	
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

	public static class ThermotechnicalDevice implements Rendering {
		public static final ThermotechnicalDevice $ = Keys.of(ThermotechnicalDevice.class);
		
		@NotEmpty
		public ThermotechnicalHeatGenerator heatGenerator;
		public ThermotechnicalDeviceHeatingType heatingType;
		public ThermotechnicalDeviceInformationSource informationSource;
		public LocalDate revisionDate;
		public Boolean heatForHeating = false;
		public Boolean heatForWarmwater = false;
		public ThermotechnicalDeviceEnergySource energySource;

		@Override
		public String render(RenderType renderType) {
			String text = EnumUtils.getText(heatGenerator);
			if (energySource != null) {
				text += " (" + EnumUtils.getText(energySource) + ")";
			}
			return text;
		}
		
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
	
	public static class BuildingEntrance implements Rendering {
		public static final BuildingEntrance $ = Keys.of(BuildingEntrance.class);
		
		public String EGAID, EDID, buildingEntranceNo;
		public EntranceStatus status;
		public Coordinates coordinates;
		public final NamedId localId = new NamedId();
		public Boolean isMainAddress, isOfficial;
		@NotEmpty
		public Locality locality;
		// @NotEmpty // TODO
		public Street street;
		
		@Override
		public String render(RenderType renderType) {
			return "Todostrasse 2, " + locality.render(RenderType.PLAIN_TEXT);
		}
	}
	
	public static enum EntranceStatus implements EchCode {
		_2001, _2002, _2003, _2004, _2005, _2007, _2008, _2009;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
	
	public static class BuildingView implements View<Building> {
		
		public static final Building $ = Keys.of(Building.class);

		public Object id;

		// einer von a, b, c
		// a)
		public Integer EGID; // ist in xsd V4 auf identification und objekt (daher kein @Enabled)
		
		// b)
		public String street, houseNumber, nameOfBuilding;
		public Integer zipCode;
		
		// c)
		public String EGRID;
		public String cadasterAreaNumber;
		public String number;
		public RealestateType realestateType;
		public String officialBuildingNo; // ist in xsd V4 auf identification und objekt (daher kein @Enabled)
		
		public List<NamedId> localID = new ArrayList<>();
		public MunicipalityIdentification municipality;
		
		//
		
		public String name;
		public BuildingDate dateOfConstruction, dateOfRenovation;
		public DatePartiallyKnown dateOfDemolition;
		public Integer numberOfFloors, numberOfSeparateHabitableRooms;
		public Integer surfaceAreaOfBuilding, subSurfaceAreaOfBuilding;
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
		public final List<ThermotechnicalDevice> thermotechnicalDevice = new ArrayList<>();

		public final List<BuildingEntrance> buildingEntrance = new ArrayList<>();
	}
}
