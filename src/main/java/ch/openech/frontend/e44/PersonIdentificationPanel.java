package ch.openech.frontend.e44;

import static ch.openech.model.person.PersonIdentification.*;
import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.model.person.PersonIdentification;

public class PersonIdentificationPanel extends EchForm<PersonIdentification> {

	public PersonIdentificationPanel() {
		this(false);
	}
	
	// TODO es gibt da noch partnerIdOrganisationType im e11, was noch nicht abgebildet ist
	public PersonIdentificationPanel(boolean partner) {
		super(2);
		TechnicalIdsFormElement technicalIdField = new TechnicalIdsFormElement($.technicalIds, partner ? TechnicalIdsFormElement.WITHOUT_EU_IDS : TechnicalIdsFormElement.WITH_EU_IDS, editable);
		
		text("<b>Hinweis:</b> Normalerweise kann eine Person über die Suche ausgewählt werden.<br>Nur bei Ausnahmen sollten hier die Identifikationsmerkmale der Person direkt eingegeben werden.");
        line($.officialName);
        line($.firstName);
        line($.dateOfBirth, $.sex);
        line($.vn, technicalIdField);
	}
	
	// TODO validierung entsprechend partner oder nicht (sex / dateOfBirth sind bei Partner nicht obligatorisch)
	
}
