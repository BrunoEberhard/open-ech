package ch.openech.frontend.e44;

import static ch.openech.model.person.PersonIdentificationLight.*;

import ch.openech.frontend.ewk.event.EchForm;
import ch.openech.model.person.PersonIdentificationLight;

public class PersonIdentificationLightPanel extends EchForm<PersonIdentificationLight> {

	public PersonIdentificationLightPanel() {
		// TechnicalIdsFormElement technicalIdField = new TechnicalIdsFormElement($.technicalIds, TechnicalIdsFormElement.WITHOUT_EU_IDS, editable);
		super(2);
		
        line($.officialName);
        line($.firstName);
        line($.dateOfBirth, $.sex);
        line($.vn);
//        line($.vn, technicalIdField);
	}
}
