package ch.openech.client.org.event;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class InLiquidationEvent extends OrganisationEventEditor<Organisation> {
	
	public InLiquidationEvent(EchSchema ech, Organisation organisation) {
		super(ech, organisation);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.liquidationEntryDate);
		formPanel.line(Organisation.ORGANISATION.liquidationReason);
		formPanel.line(Organisation.ORGANISATION.contacts);
	}

	@Override
	public Organisation load() {
		Organisation organisation = getOrganisation();
		organisation.editMode = Organisation.EditMode.IN_LIQUIDATION;
		return organisation;
	}
	
	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.inLiquidation(changedOrganisation));
	}
}
