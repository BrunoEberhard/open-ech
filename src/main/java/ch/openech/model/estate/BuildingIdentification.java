package ch.openech.model.estate;

import java.util.List;

import org.minimalj.model.View;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;

import ch.openech.model.EchFormats;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.NamedId;

@Sizes(EchFormats.class)
public class BuildingIdentification implements View<Building> {

	public Object id;
	
	// einer von a, b, c
	// a)
	public Integer EGID;
	
	// b)
	public String street, houseNumber;
	public Integer zipCode;
	@NotEmpty // min size 3
	public String nameOfBuilding;
	
	// c)
	public String EGRID;
	@Size(255) // missing size in 0129
	public String cadasterAreaNumber;
	@Size(12)
	public String number;
	public RealestateType realestateType;
	@Size(12)
	public String officialBuildingNo;
	
	public List<NamedId> localId;
	public MunicipalityIdentification municipality;
	
	/*
	<xs:sequence>
		<xs:choice>
			<xs:element name="EGID" type="eCH-0129:EGIDType"/>
			<xs:sequence>
				<xs:element name="street" type="eCH-0010:streetType"/>
				<xs:element name="houseNumber" type="eCH-0010:houseNumberType"/>
				<xs:element name="zipCode" type="eCH-0010:swissZipCodeType"/>
				<xs:element name="nameOfBuilding" type="eCH-0129:nameOfBuildingType" minOccurs="0"/>
			</xs:sequence>
			<xs:sequence>
				<xs:choice>
					<xs:element name="EGRID" type="eCH-0129:EGRIDType"/>
					<xs:sequence>
						<xs:element name="cadasterAreaNumber" type="eCH-0129:cadasterAreaNumberType"/>
						<xs:element name="number">
							<xs:simpleType>
								<xs:restriction base="xs:token">
									<xs:minLength value="1"/>
									<xs:maxLength value="12"/>
								</xs:restriction>
							</xs:simpleType>
						</xs:element>
						<xs:element name="realestateType" type="eCH-0129:realestateTypeType" minOccurs="0"/>
					</xs:sequence>
				</xs:choice>
				<xs:element name="officialBuildingNo" type="eCH-0129:officialBuildingNoType"/>
			</xs:sequence>
		</xs:choice>
		<xs:element name="localID" type="eCH-0129:namedIdType" maxOccurs="unbounded"/>
		<xs:element name="municipality" type="eCH-0007:municipalityIdType"/>
	</xs:sequence>
	*/

}
