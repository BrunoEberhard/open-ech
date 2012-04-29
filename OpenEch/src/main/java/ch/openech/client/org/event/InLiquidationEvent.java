package ch.openech.client.org.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.org.event.OrganisationEventEditor;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0148;

public class InLiquidationEvent extends OrganisationEventEditor<Organisation> {
	
	public InLiquidationEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.liquidationEntryDate);
		formPanel.line(Organisation.ORGANISATION.liquidationReason);
		formPanel.area(Organisation.ORGANISATION.contact);
		formPanel.setRequired(Organisation.ORGANISATION.liquidationEntryDate);
		formPanel.setRequired(Organisation.ORGANISATION.liquidationReason);
	}

	@Override
	public Organisation load() {
		return getOrganisation();
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.inLiquidation(changedOrganisation));
	}
}