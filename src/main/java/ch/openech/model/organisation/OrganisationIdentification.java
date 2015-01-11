package  ch.openech.model.organisation;

import java.util.Locale;

import org.minimalj.model.Rendering;
import org.minimalj.model.View;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

import ch.openech.model.EchFormats;
import ch.openech.model.common.TechnicalIds;
import ch.openech.model.organisation.types.LegalForm;

public class OrganisationIdentification implements View<Organisation>, Rendering {

	public Object id;
	
	public final UidStructure uid = new UidStructure(); 
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@Required @Size(EchFormats.organisationName)
	public String organisationName; 
	@Size(EchFormats.organisationName)
	public String organisationLegalName, organisationAdditionalName;
	public LegalForm legalForm;
	
	@Override
	public String render(RenderType renderType, Locale locale) {
		return organisationName;
	}
}
