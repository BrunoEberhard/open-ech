package ch.openech.dm.organisation;

import ch.openech.dm.EchFormats;
import ch.openech.dm.common.TechnicalIds;
import ch.openech.mj.model.annotation.Code;
import ch.openech.mj.model.annotation.Required;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.model.annotation.ViewOf;

@ViewOf(Organisation.class)
public class OrganisationIdentification {

	public final UidStructure uid = new UidStructure(); 
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@Required @Size(EchFormats.organisationName)
	public String organisationName; 
	@Size(EchFormats.organisationName)
	public String organisationLegalName, organisationAdditionalName;
	@Code
	public String legalForm;
}
