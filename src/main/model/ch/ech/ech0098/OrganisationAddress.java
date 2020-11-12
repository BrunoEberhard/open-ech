package ch.ech.ech0098;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class OrganisationAddress {
	public static final OrganisationAddress $ = Keys.of(OrganisationAddress.class);

	@Size(100)
	public String addressCategory;
	@Size(150)
	public String addressLine1;
	@Size(150)
	public String addressLine2;
	@Size(150)
	public String street;
	@Size(30)
	public String houseNumber;
	@Size(30)
	public String dwellingNumber;
	@Size(8)
	public Integer postOfficeBoxNumber;
	@NotEmpty
	@Size(40)
	public String town;
	@Size(4)
	public Integer swissZipCode;
	@Size(2)
	public String swissZipCodeAddOn;
	@Size(4)
	public Integer municipalityId;
	public ch.ech.ech0071.CantonAbbreviation cantonAbbreviation;
	@Size(9)
	public Integer EGID;
	@Size(15)
	public String foreignZipCode;
	@NotEmpty
	@Size(2)
	public String countryIdISO2;
	public Boolean deliverableYesNo;
	public LocalDate dateOfLastCheck;
}