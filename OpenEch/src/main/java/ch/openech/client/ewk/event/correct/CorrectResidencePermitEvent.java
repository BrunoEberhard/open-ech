package ch.openech.client.ewk.event.correct;

import static ch.openech.dm.person.Foreign.FOREIGN;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.event.PersonEventEditor;
import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.model.EmptyValidator;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CorrectResidencePermitEvent extends PersonEventEditor<Foreign> {
	
	public CorrectResidencePermitEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Foreign> formPanel) {
		formPanel.line(FOREIGN.residencePermit);
		formPanel.line(FOREIGN.residencePermitTill); 
	}

	@Override
	public Foreign load() {
		return getPerson().foreign;
	}
	
	@Override
	protected void validate(Foreign foreign, List<ValidationMessage> resultList) {
		super.validate(foreign, resultList);
		// bei Change ist diese Angabe obligatorisch, bei Correct nicht
		EmptyValidator.validate(resultList, foreign, FOREIGN.residencePermit);
	}

	@Override
	protected List<String> getXml(Person person, Foreign foreign, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctResidencePermit(person, foreign));
	}

}
