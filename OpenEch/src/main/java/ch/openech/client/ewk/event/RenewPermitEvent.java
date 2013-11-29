package ch.openech.client.ewk.event;

import static ch.openech.dm.person.Person.PERSON;

import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.model.EmptyValidator;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class RenewPermitEvent extends PersonEventEditor<Person> {
	
	public RenewPermitEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line(PERSON.foreign.residencePermit);
		if (echSchema.renewPermitHasTill()) {
			// bei Change ist diese Angabe obligatorisch, bei Correct und Renew nicht
			formPanel.line(PERSON.foreign.residencePermitTill);
		}
		formPanel.line(PERSON.occupation);
	}

	@Override
	public Person load() {
		return getPerson();
	}
	
	@Override
	protected List<String> getXml(Person person, Person data, WriterEch0020 writerEch0020) throws Exception {
		if (!echSchema.renewPermitHasTill()) {
			data.foreign.residencePermitTill = null;
		}
		return Collections.singletonList(writerEch0020.renewPermit(person.personIdentification, data));
	}
	
	@Override
	public void validate(Person data, List<ValidationMessage> resultList) {
		super.validate(data, resultList);
		EmptyValidator.validate(resultList, data, PERSON.foreign.residencePermit);
		if (data.occupation.isEmpty()) {
			resultList.add(new ValidationMessage(Person.PERSON.occupation, "Beruf erforderlich"));
		}
	}

}
