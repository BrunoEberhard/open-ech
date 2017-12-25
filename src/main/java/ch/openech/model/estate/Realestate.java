package ch.openech.model.estate;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.View;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

import ch.openech.model.EchFormats;
import ch.openech.model.common.NamedMetaData;

public class Realestate {

	public static final Realestate $ = Keys.of(Realestate.class);
	
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

	//
	
	public RealestateType realestateType;

	public List<NamedMetaData> namedMetaData = new ArrayList<NamedMetaData>();

	
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
	}
	
}
