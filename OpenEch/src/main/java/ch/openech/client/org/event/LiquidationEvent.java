package ch.openech.client.org.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.org.event.OrganisationEventEditor;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0148;

public class LiquidationEvent extends OrganisationEventEditor<Organisation> {
	
	public LiquidationEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(AbstractFormVisual<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.liquidationDate);
		formPanel.line(Organisation.ORGANISATION.liquidationReason);
		formPanel.setRequired(Organisation.ORGANISATION.liquidationDate);
		formPanel.setRequired(Organisation.ORGANISATION.liquidationReason);
	}

	@Override
	public Organisation load() {
		return getOrganisation();
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.liquidation(changedOrganisation));
	}
}