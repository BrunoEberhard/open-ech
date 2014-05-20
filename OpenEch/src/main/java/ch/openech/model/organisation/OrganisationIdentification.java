package  ch.openech.model.organisation;

import org.minimalj.model.annotation.Code;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.ViewOf;

import  ch.openech.model.EchFormats;
import  ch.openech.model.common.TechnicalIds;

public class OrganisationIdentification implements ViewOf<Organisation> {

	public long id;
	
	public final UidStructure uid = new UidStructure(); 
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@Required @Size(EchFormats.organisationName)
	public String organisationName; 
	@Size(EchFormats.organisationName)
	public String organisationLegalName, organisationAdditionalName;
	@Code
	public String legalForm;
	
	@Override
	public String display() {
		return organisationName;
	}
}
