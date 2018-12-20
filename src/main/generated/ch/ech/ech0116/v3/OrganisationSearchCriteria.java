package ch.ech.ech0116.v3;

import java.util.List;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2018-12-20T07:58:10.140")
public class OrganisationSearchCriteria {
	public static final OrganisationSearchCriteria $ = Keys.of(OrganisationSearchCriteria.class);

	public Object id;
	public ch.openech.xml.UidStructure uid;
	public List<ch.openech.xml.NamedId> OtherOrganisationId;
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
}