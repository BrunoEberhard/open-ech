package ch.openech.model.estate;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;

import ch.openech.model.common.Address;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.model.person.PersonIdentification;

public class RealestateInformation implements Rendering {

	public static final RealestateInformation $ = Keys.of(RealestateInformation.class);
	
	public Object id;
	
	@NotEmpty
	public Realestate realestate;
	@NotEmpty
	public MunicipalityIdentification municipality;
	// building(Information) is on realestate 
	public final PlaceName placeName = new PlaceName();
	public List<Owner> owner;
	
	@Override
	public String render(RenderType renderType) {
		return realestate.render(RenderType.PLAIN_TEXT) + " in " + municipality.municipalityName;
	}
	
	public static class Owner {
		public PersonIdentification personIdentification;
		public OrganisationIdentification organisationIdentification;
		public Address ownerAdress;
	}

	/*
	 	<xs:sequence>
			<xs:element name="realestate" type="eCH-0129:realestateType" />
			<xs:element name="municipality" type="eCH-0007:swissMunicipalityType" />
			<xs:element name="buildingInformation" minOccurs="0" maxOccurs="unbounded">
				<xs:complexType>
					<xs:sequence>
						<xs:element name="building" type="eCH-0129:buildingType" />
						<xs:element name="dwelling" type="eCH-0129:dwellingType" minOccurs="0" maxOccurs="unbounded" />
					</xs:sequence>
				</xs:complexType>
			</xs:element>
			<xs:element name="placeName" type="eCH-0129:placeNameType" />
			<xs:element name="owner" maxOccurs="unbounded">
				<xs:complexType>
					<xs:sequence>
						<xs:element name="ownerIdentification" minOccurs="0">
							<xs:complexType>
								<xs:choice>
									<xs:element name="personIdentification" type="eCH-0044:personIdentificationLightType" />
									<xs:element name="organisationIdentification" type="eCH-0097:organisationIdentificationType" />
								</xs:choice>
							</xs:complexType>
						</xs:element>
						<xs:element name="ownerAdress" type="eCH-0010:mailAddressType" />
					</xs:sequence>
				</xs:complexType>
			</xs:element>
		</xs:sequence>
		
	 */

}
