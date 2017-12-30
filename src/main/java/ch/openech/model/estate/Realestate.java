package ch.openech.model.estate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.View;
import org.minimalj.model.annotation.Decimal;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Searched;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;

import ch.openech.model.EchFormats;
import ch.openech.model.common.NamedMetaData;
import ch.openech.model.types.EchCode;

@Sizes(EchFormats.class)
public class Realestate implements Rendering {

	public static final Realestate $ = Keys.of(Realestate.class);
	
	public Object id;
	
	@Size(EchFormats.EGRID) @Searched
	public String EGRID;
	@Size(12) @NotEmpty
	public String number;
	@Size(12)
	public String numberSuffix;
	@Size(15)
	public String subDistrict;
	@Size(15)
	public String lot;

	//
	
	public String authority;
	public LocalDate date;
	public RealestateType realestateType;
	public String cantonalSubKind;
	public RealestateStatus status;
	@Size(EchFormats.realestateMutnumber)
	public String mutnumber;
	public String identDN;
	@Size(10) @Decimal(1)
	public BigDecimal squareMeasure;
	public Boolean realestateIncomplete;
	public Coordinates coordinates;
	public List<NamedMetaData> namedMetaData = new ArrayList<NamedMetaData>();

	// nicht bei eCH / View
	public List<Building> building = new ArrayList<>();
	
	@Override
	public String render(RenderType renderType) {
		return EGRID;
	}
	
	/*
		<xs:sequence>
			<xs:element name="realestateIdentification" type="eCH-0129:realestateIdentificationType"/>
			<xs:element name="authority" type="eCH-0129:authorityType" minOccurs="0"/>
			<xs:element name="date" type="eCH-0129:realestateDateType" minOccurs="0"/>
			<xs:element name="realestateType" type="eCH-0129:realestateTypeType"/>
			<xs:element name="cantonalSubKind" type="eCH-0129:cantonalSubKindType" minOccurs="0"/>
			<xs:element name="status" type="eCH-0129:realestateStatusType" minOccurs="0"/>
			<xs:element name="mutnumber" type="eCH-0129:realestateMutnumberType" minOccurs="0"/>
			<xs:element name="identDN" type="eCH-0129:identDNType" minOccurs="0"/>
			<xs:element name="squareMeasure" type="eCH-0129:squareMeasureType" minOccurs="0"/>
			<xs:element name="realestateIncomplete" type="eCH-0129:realestateIncompleteType" minOccurs="0"/>
			<xs:element name="coordinates" type="eCH-0129:coordinatesType" minOccurs="0"/>
			<xs:element name="namedMetaData" type="eCH-0129:namedMetaDataType" minOccurs="0" maxOccurs="unbounded"/>
		</xs:sequence>	 
	 
	 */

	
	public static class RealestateIdentification implements View<Realestate> {

		public Object id;
		
		@Size(EchFormats.EGRID)
		public String EGRID;
		@Size(12) @NotEmpty
		public String number;
		@Size(12)
		public String numberSuffix;
		@Size(15)
		public String subDistrict;
		@Size(15)
		public String lot;
		
		/*
		 <xs:sequence>
			<xs:element name="EGRID" minOccurs="0">
				<xs:simpleType>
					<xs:restriction base="eCH-0129:EGRIDType"/>
				</xs:simpleType>
			</xs:element>
			<xs:element name="number">
				<xs:simpleType>
					<xs:restriction base="xs:token">
						<xs:minLength value="1"/>
						<xs:maxLength value="12"/>
					</xs:restriction>
				</xs:simpleType>
			</xs:element>
			<xs:element name="numberSuffix" minOccurs="0">
				<xs:simpleType>
					<xs:restriction base="xs:token">
						<xs:minLength value="1"/>
						<xs:maxLength value="12"/>
					</xs:restriction>
				</xs:simpleType>
			</xs:element>
			<xs:element name="subDistrict" minOccurs="0">
				<xs:simpleType>
					<xs:restriction base="xs:token">
						<xs:minLength value="1"/>
						<xs:maxLength value="15"/>
					</xs:restriction>
				</xs:simpleType>
			</xs:element>
			<xs:element name="lot" minOccurs="0">
				<xs:simpleType>
					<xs:restriction base="xs:token">
						<xs:minLength value="1"/>
						<xs:maxLength value="15"/>
					</xs:restriction>
				</xs:simpleType>
			</xs:element>
		</xs:sequence>
		
		 */
	}
	
	public static enum RealestateStatus implements EchCode {
		_0, _1;
		
		@Override
		public String getValue() {
			return String.valueOf(ordinal() + 1);
		}
	}
	
	public class RealestateView implements View<Realestate>, Rendering {
		
		public Object id;
		public String EGRID;
		public String number;
		public String numberSuffix;
		public String subDistrict;
		public String lot;

		//
		
		public String authority;
		public LocalDate date;
		public RealestateType realestateType;
		public String cantonalSubKind;
		public RealestateStatus status;
		public String mutnumber;
		public String identDN;
		public BigDecimal squareMeasure;
		public Boolean realestateIncomplete;
		public Coordinates coordinates;
		public List<NamedMetaData> namedMetaData = new ArrayList<NamedMetaData>();

		@Override
		public String render(RenderType renderType) {
			return EGRID;
		}
	}
	
}
