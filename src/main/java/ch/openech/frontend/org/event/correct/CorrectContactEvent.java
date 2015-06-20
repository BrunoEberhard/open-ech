package ch.openech.frontend.org.event.correct;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;

import ch.openech.frontend.org.event.OrganisationEventEditor;
import ch.openech.frontend.page.OrganisationPage;
import ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.WriterEch0148;

public class CorrectContactEvent extends OrganisationEventEditor<Organisation> {
	
	public CorrectContactEvent(OrganisationPage organisationPage) {
		super(organisationPage);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.$.contacts);
	}

	@Override
	public Organisation createObject() {
		return getOrganisation();
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.correctContact(changedOrganisation));
	}
}
