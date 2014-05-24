package ch.openech.frontend.org.event.correct;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;

import ch.openech.frontend.org.event.OrganisationEventEditor;
import  ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class CorrectLiquidationEvent extends OrganisationEventEditor<Organisation> {
	
	public CorrectLiquidationEvent(EchSchema ech, Organisation organisation) {
		super(ech, organisation);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.liquidationEntryDate);
		formPanel.line(Organisation.ORGANISATION.liquidationDate);
		formPanel.line(Organisation.ORGANISATION.liquidationReason);
		formPanel.line(Organisation.ORGANISATION.contacts);
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