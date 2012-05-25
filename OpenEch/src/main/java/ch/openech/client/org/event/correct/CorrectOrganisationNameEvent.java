package ch.openech.client.org.event.correct;

import java.util.Collections;
import java.util.List;

import ch.openech.client.org.event.OrganisationEventEditor;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class CorrectOrganisationNameEvent extends OrganisationEventEditor<Organisation> {
	
	public CorrectOrganisationNameEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.area(Organisation.ORGANISATION.organisationName);
		formPanel.area(Organisation.ORGANISATION.organisationLegalName);
		formPanel.area(Organisation.ORGANISATION.organisationAdditionalName);
	}

	@Override
	public Organisation load() {
		return getOrganisation();
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.changeOrganisationName(changedOrganisation));
	}
}