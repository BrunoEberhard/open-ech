package ch.openech.client.org.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.org.event.OrganisationEventEditor;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0148;

public class ChangeOrganisationNameEvent extends OrganisationEventEditor<Organisation> {
	
	public ChangeOrganisationNameEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
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
		return Collections.singletonList(writerEch0148.correctOrganisationName(changedOrganisation));
	}
}