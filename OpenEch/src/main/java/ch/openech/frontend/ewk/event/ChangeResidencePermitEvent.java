package ch.openech.frontend.ewk.event;

import static ch.openech.model.person.Person.*;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;
import org.minimalj.model.validation.EmptyValidator;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.model.person.Person;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;


public class ChangeResidencePermitEvent extends PersonEventEditor<Person> {

	public ChangeResidencePermitEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line($.foreign.residencePermit);
		formPanel.line($.foreign.residencePermitTill);
		formPanel.line($.occupation);
	}

	@Override
	public Person load() {
		return getPerson();
	}

	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		super.validate(person, resultList);
		EmptyValidator.validate(resultList, person, person.foreign.residencePermit);
		// bei Change ist diese Angabe obligatorisch, bei Correct nicht
		EmptyValidator.validate(resultList, person, person.foreign.residencePermitTill);
	}

	@Override
	protected List<String> getXml(Person person, Person changedPerson, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.changeResidencePermit(changedPerson));
    }

}
