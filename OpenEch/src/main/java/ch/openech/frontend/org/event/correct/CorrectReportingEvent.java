package ch.openech.frontend.org.event.correct;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.edit.form.Form;

import ch.openech.frontend.org.OrganisationPanel;
import ch.openech.frontend.org.event.OrganisationEventEditor;
import  ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class CorrectReportingEvent extends OrganisationEventEditor<Organisation> {
	
	public CorrectReportingEvent(EchSchema ech, Organisation organisation) {
		super(ech, organisation);
	}

	@Override
	public Form<Organisation> createForm() {
		return new OrganisationPanel(Organisation.EditMode.CHANGE_RESIDENCE_TYPE, echSchema);
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
