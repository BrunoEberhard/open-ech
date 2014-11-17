package ch.openech.frontend.org.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;

import  ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class ChangeReportingEvent extends OrganisationEventEditor<Organisation> {
	
	public ChangeReportingEvent(EchSchema ech, Organisation organisation) {
		super(ech, organisation);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.$.typeOfResidenceOrganisation, Organisation.$.reportingMunicipality);
		formPanel.line(Organisation.$.arrivalDate, Organisation.$.comesFrom);
		formPanel.line(Organisation.$.businessAddress);
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
