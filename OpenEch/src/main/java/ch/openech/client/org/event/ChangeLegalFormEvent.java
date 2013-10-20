package ch.openech.client.org.event;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.edit.form.Form;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class ChangeLegalFormEvent extends OrganisationEventEditor<Organisation> {

	public ChangeLegalFormEvent(EchSchema ech, Organisation organisation) {
		super(ech, organisation);
	}

	@Override
	protected Organisation load() {
		return getOrganisation();
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.identification.legalForm);
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation object, WriterEch0148 writerEch0148)
			throws Exception {
		return Collections.singletonList(writerEch0148.changeLegalForm(organisation));
	}

}
