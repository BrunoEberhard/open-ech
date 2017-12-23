package ch.openech.model.estate;

import java.util.List;

import org.minimalj.model.annotation.Sizes;

import ch.openech.model.EchFormats;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.NamedId;

@Sizes(EchFormats.class)
public class Dwelling {

	public List<NamedId> localID;
	public String administrativeDwellingNo;
	public String EWID;
	public String physicalDwellingNo;
	public DatePartiallyKnown dateOfConstruction, dateOfDemolition;
	public Integer noOfHabitableRooms;
	public Integer floor;
	public String locationOfDwellingOnFloor;
	public Boolean multipleFloor;
	public UsageLimitation usageLimitation;
	public Kitchen kitchen;
	public Integer surfaceAreaOfDwelling;
	public DwellingStatus dwellingStatus;
	public DwellingUsage dwellingUsage;
	
/*
	<xs:sequence>
		<xs:element name="localID" type="eCH-0129:namedIdType" maxOccurs="unbounded"/>
		<xs:element name="administrativeDwellingNo" type="eCH-0129:administrativeDwellingNoType" minOccurs="0"/>
		<xs:element name="EWID" type="eCH-0129:EWIDType" minOccurs="0"/>
		<xs:element name="physicalDwellingNo" type="eCH-0129:physicalDwellingNoType" minOccurs="0"/>
		<xs:element name="dateOfConstruction" type="eCH-0129:datePartiallyKnownType" minOccurs="0"/>
		<xs:element name="dateOfDemolition" type="eCH-0129:datePartiallyKnownType" minOccurs="0"/>
		<xs:element name="noOfHabitableRooms" type="eCH-0129:noOfHabitableRoomsType" minOccurs="0"/>
		<xs:element name="floor" type="eCH-0129:floorType" minOccurs="0"/>
		<xs:element name="locationOfDwellingOnFloor" type="eCH-0129:locationOfDwellingOnFloorType" minOccurs="0"/>
		<xs:element name="multipleFloor" type="eCH-0129:multipleFloorType" minOccurs="0"/>
		<xs:element name="usageLimitation" type="eCH-0129:usageLimitationType" minOccurs="0"/>
		<xs:element name="kitchen" type="eCH-0129:kitchenType" minOccurs="0"/>
		<xs:element name="surfaceAreaOfDwelling" type="eCH-0129:surfaceAreaOfDwellingType" minOccurs="0"/>
		<xs:element name="status" type="eCH-0129:dwellingStatusType" minOccurs="0"/>
		<xs:element name="dwellingUsage" type="eCH-0129:dwellingUsageType" minOccurs="0"/>
	</xs:sequence>
*/
	
}
