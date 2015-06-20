package ch.openech.frontend.ewk.event;

import static ch.openech.model.person.Person.*;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.model.validation.EmptyValidator;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Person;
import ch.openech.xml.write.WriterEch0020;

public class RenewPermitEvent extends PersonEventEditor<Person> {
	
	public RenewPermitEvent(PersonPage personPage) {
		super(personPage);
	}

	@Override
	protected void fillForm(Form<Person> formPanel) {
		formPanel.line($.foreign.residencePermit);
		if (echSchema.renewPermitHasTill()) {
			// bei Change ist diese Angabe obligatorisch, bei Correct und Renew nicht
			formPanel.line($.foreign.residencePermitTill);
		}
		formPanel.line($.occupation);
	}

	@Override
	public Person createObject() {
		return getPerson();
	}
	
	@Override
	protected List<String> getXml(Person person, Person data, WriterEch0020 writerEch0020) throws Exception {
		if (!echSchema.renewPermitHasTill()) {
			data.foreign.residencePermitTill = null;
		}
		return Collections.singletonList(writerEch0020.renewPermit(person.personIdentification(), data));
	}
	
	@Override
	public void validate(Person data, List<ValidationMessage> resultList) {
		super.validate(data, resultList);
		EmptyValidator.validate(resultList, data, $.foreign.residencePermit);
		if (data.occupation.isEmpty()) {
			resultList.add(new ValidationMessage(Person.$.occupation, "Beruf erforderlich"));
		}
	}

}
