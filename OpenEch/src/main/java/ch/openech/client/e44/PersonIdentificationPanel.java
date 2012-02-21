package ch.openech.client.e44;

import static ch.openech.dm.person.PersonIdentification.PERSON_IDENTIFICATION;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.edit.fields.DateField;

//setTitle("Personenidentifikation");
//setInformation("<html><b>Hinweis:</b> Normalerweise kann eine Person über die Suche ausgewählt werden.<br>Nur bei Ausnahmen sollten hier die Identifikationsmerkmale der Person direkt eingegeben werden.</html>");
public class PersonIdentificationPanel extends EchFormPanel<PersonIdentification> {

	public PersonIdentificationPanel() {
		this(false);
	}
	
	// TODO es gibt da noch partnerIdOrganisationType im e11, was noch nicht abgebildet ist
	public PersonIdentificationPanel(boolean partner) {
		super();
		TechnicalIdsField technicalIdField = new TechnicalIdsField(PERSON_IDENTIFICATION.technicalIds, partner ? TechnicalIdsField.WITHOUT_EU_IDS : TechnicalIdsField.WITH_EU_IDS, editable);
		
		text("<html><b>Hinweis:</b> Normalerweise kann eine Person über die Suche ausgewählt werden.<br>Nur bei Ausnahmen sollten hier die Identifikationsmerkmale der Person direkt eingegeben werden.</html>");
        line(PERSON_IDENTIFICATION.officialName);
        line(PERSON_IDENTIFICATION.firstName);
        line(new DateField(PERSON_IDENTIFICATION.dateOfBirth, DateField.NOT_REQUIRED), PERSON_IDENTIFICATION.sex);
        line(PERSON_IDENTIFICATION.vn, technicalIdField);
	}
	
	// TODO validierung entsprechend partner oder nicht (sex / dateOfBirth sind bei Partner nicht obligatorisch)
	
}