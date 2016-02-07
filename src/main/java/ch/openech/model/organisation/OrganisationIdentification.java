package  ch.openech.model.organisation;

import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

import ch.openech.model.EchFormats;
import ch.openech.model.common.TechnicalIds;
import ch.openech.model.organisation.types.LegalForm;

public class OrganisationIdentification implements Rendering {

	public Object id;
	
	public final UidStructure uid = new UidStructure(); 
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@NotEmpty @Size(EchFormats.organisationName)
	public String organisationName; 
	@Size(EchFormats.organisationName)
	public String organisationLegalName, organisationAdditionalName;
	public LegalForm legalForm;
	
	@Override
	public String render(RenderType renderType) {
		return organisationName;
	}
}
