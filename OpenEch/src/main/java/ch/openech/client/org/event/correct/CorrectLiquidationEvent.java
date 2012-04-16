package ch.openech.client.org.event.correct;

import java.util.Collections;
import java.util.List;

import ch.openech.client.org.event.OrganisationEventEditor;
import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0148;

public class CorrectLiquidationEvent extends OrganisationEventEditor<Organisation> {
	
	public CorrectLiquidationEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	protected void fillForm(AbstractFormVisual<Organisation> formPanel) {
		formPanel.area(Organisation.ORGANISATION.liquidationEntryDate);
		formPanel.area(Organisation.ORGANISATION.liquidationDate);
		formPanel.area(Organisation.ORGANISATION.liquidationReason);
		formPanel.area(Organisation.ORGANISATION.contact);
	}

	@Override
	public Organisation load() {
		return getOrganisation();
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.correctLiquidation(changedOrganisation));
	}
}