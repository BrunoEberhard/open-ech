package ch.openech.client.org.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.model.EmptyValidator;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class MoveOutEvent extends OrganisationEventEditor<Organisation> {
	
	public MoveOutEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.reportingMunicipality);
		formPanel.line(Organisation.ORGANISATION.departureDate);
		formPanel.area(Organisation.ORGANISATION.goesTo);
	}

	
	
	@Override
	protected void validate(Organisation object, List<ValidationMessage> resultList) {
		super.validate(object, resultList);
		EmptyValidator.validate(resultList, object, Organisation.ORGANISATION.departureDate);
		EmptyValidator.validate(resultList, object, Organisation.ORGANISATION.goesTo);
	}

	@Override
	public Organisation load() {
		return getOrganisation();
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.moveOut(changedOrganisation));
	}
}
