package ch.openech.client.org.event;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0148;

public class ChangeReportingEvent extends OrganisationEventEditor<Organisation> {
	
	public ChangeReportingEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(AbstractFormVisual<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.typeOfResidenceOrganisation, Organisation.ORGANISATION.reportingMunicipality);
		formPanel.line(Organisation.ORGANISATION.arrivalDate, Organisation.ORGANISATION.comesFrom);
		formPanel.area(Organisation.ORGANISATION.businessAddress);
		formPanel.setRequired(Organisation.ORGANISATION.typeOfResidenceOrganisation);
		formPanel.setRequired(Organisation.ORGANISATION.reportingMunicipality);
	}

	@Override
	public Organisation load() {
		return getOrganisation();
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.changeReporting(changedOrganisation));
	}
}