package ch.openech.client.org.event;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class LiquidationEvent extends OrganisationEventEditor<Organisation> {
	
	public LiquidationEvent(EchSchema ech, Organisation organisation) {
		super(ech, organisation);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.liquidationDate);
		formPanel.line(Organisation.ORGANISATION.liquidationReason);
	}

	@Override
	public Organisation load() {
		Organisation organisation = getOrganisation();
		organisation.editMode = Organisation.EditMode.LIQUIDATION;
		return organisation;
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.liquidation(changedOrganisation));
	}
}
