package ch.openech.client.org.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class ChangeLegalFormEvent extends OrganisationEventEditor<Organisation> {

	public ChangeLegalFormEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
	}

	@Override
	protected Organisation load() {
		return getOrganisation();
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.legalForm);
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation object, WriterEch0148 writerEch0148)
			throws Exception {
		return Collections.singletonList(writerEch0148.changeLegalForm(organisation));
	}

}
