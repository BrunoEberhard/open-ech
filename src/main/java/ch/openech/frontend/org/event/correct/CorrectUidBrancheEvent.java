package ch.openech.frontend.org.event.correct;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.model.validation.EmptyValidator;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.frontend.org.event.OrganisationEventEditor;
import  ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class CorrectUidBrancheEvent extends OrganisationEventEditor<Organisation> {
	
	public CorrectUidBrancheEvent(EchSchema ech, Organisation organisation) {
		super(ech, organisation);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.$.uidBrancheText);
		formPanel.line(Organisation.$.nogaCode);
	}

	@Override
	public Organisation load() {
		return getOrganisation();
	}
	
	@Override
	protected void validate(Organisation object, List<ValidationMessage> resultList) {
		super.validate(object, resultList);
		EmptyValidator.validate(resultList, object, Organisation.$.uidBrancheText);
		EmptyValidator.validate(resultList, object, Organisation.$.nogaCode);
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.correctUidBranche(changedOrganisation));
	}
}
