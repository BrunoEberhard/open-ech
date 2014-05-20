package ch.openech.frontend.org.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.EmptyValidator;
import org.minimalj.model.validation.ValidationMessage;

import  ch.openech.model.organisation.Organisation;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0148;

public class MoveOutEvent extends OrganisationEventEditor<Organisation> {
	
	public MoveOutEvent(EchSchema ech, Organisation organisation) {
		super(ech, organisation);
	}

	@Override
	protected void fillForm(Form<Organisation> formPanel) {
		formPanel.line(Organisation.ORGANISATION.reportingMunicipality);
		formPanel.line(Organisation.ORGANISATION.departureDate);
		formPanel.line(Organisation.ORGANISATION.goesTo);
	}

	
	
	@Override
	protected void validate(Organisation object, List<ValidationMessage> resultList) {
		super.validate(object, resultList);
		EmptyValidator.validate(resultList, object, Organisation.ORGANISATION.departureDate);
		EmptyValidator.validate(resultList, object, Organisation.ORGANISATION.goesTo);
	}

	@Override
	public Organisation load() {
		return getOrganisation();
	}

	@Override
	protected List<String> getXml(Organisation organisation, Organisation changedOrganisation, WriterEch0148 writerEch0148) throws Exception {
		return Collections.singletonList(writerEch0148.moveOut(changedOrganisation));
	}
}
