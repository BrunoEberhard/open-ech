package ch.ech.ech0116.v4;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

// handmade
public class OrganisationSearchCriteria {
	public static final OrganisationSearchCriteria $ = Keys.of(OrganisationSearchCriteria.class);

	public Object id;
	public ch.openech.model.UidStructure uid;
	public List<ch.openech.model.NamedId> OtherOrganisationId;
	@Size(255)
	public String organisationName;
	@Size(255)
	public String organisationAdditionalName;
	@Size(6)
	public String NOGACode;
	public ch.ech.ech0098.Foundation foundation;
	public ch.ech.ech0098.Liquidation liquidation;
	@Size(255) // unknown
	public String languageOfCorrespondance;
	public ch.ech.ech0108.UidregInformation uidregInformation;
	public ch.ech.ech0108.CommercialRegisterInformation commercialRegisterInformation;
	public ch.ech.ech0108.VatRegisterInformation vatRegisterInformation;
	@Size(4)
	public Integer organisationMunicipalityID;
	public ch.ech.ech0071.CantonAbbreviation cantonAbbreviationMainAddress;
	public ch.ech.ech0071.CantonAbbreviation cantonAbbreviationAdditionalAddress;
	
	//
	
	public String getNogaCode() {
		return NOGACode;
	}
	
	public void setNogaCode(String nogaCode) {
		this.NOGACode = nogaCode;
	}
	
}