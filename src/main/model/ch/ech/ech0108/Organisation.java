package ch.ech.ech0108;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

import ch.ech.ech0007.SwissMunicipality;

// handmade
public class Organisation {
	public static final Organisation $ = Keys.of(Organisation.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0098.Organisation organisation;
	public UidregInformation uidregInformation;
	public CommercialRegisterInformation commercialRegisterInformation;
	public VatRegisterInformation vatRegisterInformation;
	public LeiRegisterInformation leiRegisterInformation;
	public List<GroupRelationship> groupRelationship;
	public List<InvolvedPerson> involvedPerson;
	
	// in version 2
	public Integer organisationMunicipalityID;

	// in version 3
	public SwissMunicipality organisationMunicipality;
	
	// in version 2 und 3
	@Size(2)
	public String cantonAbbreviationMainAddress, cantonAbbreviationAdditionalAddress;
}