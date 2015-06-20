package ch.openech.frontend.org.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;

import ch.openech.frontend.page.OrganisationPage;
import ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.WriterEch0148;

public class MoveEvent extends OrganisationEventEditor<Organisation> {
	
	public MoveEvent(OrganisationPage organisationPage) {
		super(organisationPage);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.$.reportingMunicipality);
		formPanel.line(Organisation.$.businessAddress);
	}

	@Override
	public Organisation createObject() {
		return getOrganisation();
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.move(changedOrganisation));
	}
}
