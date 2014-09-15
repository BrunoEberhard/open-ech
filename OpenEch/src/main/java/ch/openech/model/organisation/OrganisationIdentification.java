package  ch.openech.model.organisation;

import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.ViewOf;

import ch.openech.model.EchFormats;
import ch.openech.model.common.TechnicalIds;
import ch.openech.model.organisation.types.LegalForm;

public class OrganisationIdentification implements ViewOf<Organisation> {

	public Object id;
	
	public final UidStructure uid = new UidStructure(); 
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@Required @Size(EchFormats.organisationName)
	public String organisationName; 
	@Size(EchFormats.organisationName)
	public String organisationLegalName, organisationAdditionalName;
	public LegalForm legalForm;
	
	@Override
	public String display() {
		return organisationName;
	}
}
