package ch.openech.model.estate;

import java.time.LocalDate;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;

import ch.openech.model.EchFormats;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.NamedId;
import ch.openech.model.types.EchCode;

@Sizes(EchFormats.class)
public class Dwelling {

	public static final Dwelling $ = Keys.of(Dwelling.class);
	
	public Object id;

	public List<NamedId> localID;
	public String administrativeDwellingNo;
	public String EWID;
	public String physicalDwellingNo;
	public DatePartiallyKnown dateOfConstruction, dateOfDemolition;
	public Integer noOfHabitableRooms;
	public Integer floor;
	public String locationOfDwellingOnFloor;
	public Boolean multipleFloor = false;
	public UsageLimitation usageLimitation;
	public Kitchen kitchen;
	public Integer surfaceAreaOfDwelling;
	public DwellingStatus dwellingStatus;
	public final DwellingUsage dwellingUsage = new DwellingUsage();

	
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
	public static class DwellingUsage {
		public DwellingUsageCode usageCode;
		public DwellingInformationSource informationSource;
		public LocalDate revisionDate; // TODO validation
		@Size(2000)
		public String remark;
		public Boolean personWithMainResidence = true;
		public Boolean personWithSecondaryResidence = false;
		public LocalDate dateFirstOccupancy; // TODO validation
		public LocalDate dateLastOccupancy; // TODO validation
	}
	
	
	public static enum DwellingStatus implements EchCode {

		_3001, _3002, _3003, _3004, _3005, _3007, _3008;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
	
	public enum DwellingUsageCode implements EchCode {

		_3010, _3020, _3030, _3031, _3032, _3033, _3034, _3035, _3036, _3037, _3038, _3070;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
	
	public enum DwellingInformationSource implements EchCode {

		_3090, _3091, _3092, _3093;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
	
	public static enum Kitchen implements EchCode {

		_7300, _7301, _7302;

		@Override
		public String getValue() {
			return name().substring(1);
		}
	}
}
