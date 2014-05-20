package ch.openech.frontend.org.event.correct;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;

import ch.openech.frontend.org.event.OrganisationEventEditor;
import  ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class CorrectOrganisationNameEvent extends OrganisationEventEditor<Organisation> {
	
	public CorrectOrganisationNameEvent(EchSchema ech, Organisation organisation) {
		super(ech, organisation);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.organisationName);
		formPanel.line(Organisation.ORGANISATION.organisationLegalName);
		formPanel.line(Organisation.ORGANISATION.organisationAdditionalName);
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
