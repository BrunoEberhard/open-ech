package ch.openech.frontend.org.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;

import ch.openech.frontend.page.OrganisationPage;
import ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.WriterEch0148;

public class LiquidationEvent extends OrganisationEventEditor<Organisation> {
	
	public LiquidationEvent(OrganisationPage organisationPage) {
		super(organisationPage);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.$.liquidationDate);
		formPanel.line(Organisation.$.liquidationReason);
	}

	@Override
	public Organisation createObject() {
		Organisation organisation = getOrganisation();
		organisation.editMode = Organisation.EditMode.LIQUIDATION;
		return organisation;
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.liquidation(changedOrganisation));
	}
}
