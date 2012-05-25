package ch.openech.client.org.event.correct;

import java.util.Collections;
import java.util.List;

import ch.openech.client.org.OrganisationPanel;
import ch.openech.client.org.OrganisationPanel.OrganisationPanelType;
import ch.openech.client.org.event.OrganisationEventEditor;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class CorrectReportingEvent extends OrganisationEventEditor<Organisation> {
	
	public CorrectReportingEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
	}

	@Override
	public IForm<Organisation> createForm() {
		return new OrganisationPanel(OrganisationPanelType.CHANGE_RESIDENCE_TYPE, echSchema);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		// not used
	}

	@Override
	public Organisation load() {
		return getOrganisation();
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.correctReporting(changedOrganisation));
	}
}
