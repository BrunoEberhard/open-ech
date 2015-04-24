package ch.openech.frontend.ewk.event.correct;

import static  ch.openech.model.person.Foreign.*;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.model.validation.EmptyValidator;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.frontend.ewk.event.PersonEventEditor;
import  ch.openech.model.person.Foreign;
import  ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class CorrectResidencePermitEvent extends PersonEventEditor<Foreign> {
	
	public CorrectResidencePermitEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Foreign> formPanel) {
		formPanel.line($.residencePermit);
		formPanel.line($.residencePermitTill); 
	}

	@Override
	public Foreign load() {
		return getPerson().foreign;
	}
	
	@Override
	protected void validate(Foreign foreign, List<ValidationMessage> resultList) {
		super.validate(foreign, resultList);
		// bei Change ist diese Angabe obligatorisch, bei Correct nicht
		EmptyValidator.validate(resultList, foreign, $.residencePermit);
	}

	@Override
	protected List<String> getXml(Person person, Foreign foreign, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.correctResidencePermit(person, foreign));
	}

}
