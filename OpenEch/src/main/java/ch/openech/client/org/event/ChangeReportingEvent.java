package ch.openech.client.org.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class ChangeReportingEvent extends OrganisationEventEditor<Organisation> {
	
	public ChangeReportingEvent(EchSchema ech, Organisation organisation) {
		super(ech, organisation);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.typeOfResidenceOrganisation, Organisation.ORGANISATION.reportingMunicipality);
		formPanel.line(Organisation.ORGANISATION.arrivalDate, Organisation.ORGANISATION.comesFrom);
		formPanel.line(Organisation.ORGANISATION.businessAddress);
	}

	@Override
	protected int getFormColumns() {
		return 2;
	}
	
	@Override
	public Organisation load() {
		Organisation organisation = getOrganisation();
		organisation.editMode = Organisation.EditMode.CHANGE_REPORTING;
		return organisation;
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.changeReporting(changedOrganisation));
	}
}
